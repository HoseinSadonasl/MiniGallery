package com.hotaku.media_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.ui.MediaType
import com.hotaku.ui.conposables.AnimatedMediaDetailCompactTopBar
import com.hotaku.ui.conposables.AnimatedMediaDetailExpendedTopBar
import com.hotaku.ui.conposables.MediaDetail
import com.hotaku.ui.conposables.MediaDetailPager
import com.hotaku.ui.conposables.MediaDetailSurface
import com.hotaku.ui.conposables.MediaOptions
import com.hotaku.ui.conposables.noRippleClickable
import com.hotaku.ui.rememberLauncherForStartIntentSenderForResult
import com.hotaku.ui.sendPlayIntent
import com.hotaku.ui.sendShareIntent
import com.hotaku.ui.trashMediaItemByUri
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun MediaDetailScreen(
    modifier: Modifier = Modifier,
    mediaDetailViewModel: MediaDetailViewModel,
    navigateUp: () -> Unit,
) {
    MediaDetailScreen(
        modifier = modifier,
        mediaDetailViewModel = mediaDetailViewModel,
        navigateUp = navigateUp,
        onAction = mediaDetailViewModel::onAction,
    )
}

@Composable
private fun MediaDetailScreen(
    modifier: Modifier = Modifier,
    mediaDetailViewModel: MediaDetailViewModel,
    navigateUp: () -> Unit,
    onAction: (MediaDetailScreenActions) -> Unit,
) {
    val context = LocalContext.current

    val state by mediaDetailViewModel.mediaDetailUiState.collectAsStateWithLifecycle()

    val pagerMediaItems = mediaDetailViewModel.mediaUiState.collectAsLazyPagingItems()

    val windowWidth = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

    val title: String =
        remember(
            key1 = pagerMediaItems.itemCount,
            key2 = state.selectedMediaItemIndex,
        ) {
            if (pagerMediaItems.itemCount > 0) pagerMediaItems.peek(state.selectedMediaItemIndex)?.displayName.orEmpty() else ""
        }

    val trashLauncher =
        rememberLauncherForStartIntentSenderForResult {
            state.selectedMediaItemIndex.let { index ->
                pagerMediaItems.peek(index)?.let { media ->
                    onAction(
                        MediaDetailScreenActions.OnDeleteMedia(
                            mediaItem = media,
                        ),
                    )
                }
            }
        }

    LaunchedEffect(state.isOptionsVisible) {
        if (state.isOptionsVisible) {
            delay(1500)
            onAction(MediaDetailScreenActions.OnHideOptions)
        }
    }

    LaunchedEffect(mediaDetailViewModel.mediaDetailUiEvents) {
        mediaDetailViewModel.mediaDetailUiEvents.collectLatest { event ->
            when (event) {
                MediaDetailScreenEvents.OnRefreshMedia -> {
                    pagerMediaItems.refresh()
                }
                MediaDetailScreenEvents.OnShareMedia -> {
                    pagerMediaItems.peek(state.selectedMediaItemIndex)?.sendShareIntent(context = context)
                }
                MediaDetailScreenEvents.OnPlayVideo -> {
                    pagerMediaItems.peek(state.selectedMediaItemIndex)?.sendPlayIntent(context = context)
                }
            }
        }
    }

    MediaDetailSurface(
        modifier =
            modifier
                .noRippleClickable {
                    onAction(MediaDetailScreenActions.OnShowOptions)
                },
        topContent = {
            Box(modifier = Modifier.statusBarsPadding()) {
                if (windowWidth == WindowWidthSizeClass.COMPACT) {
                    AnimatedMediaDetailCompactTopBar(
                        title = title,
                        show = state.isOptionsVisible,
                        onClose = navigateUp,
                    )
                } else {
                    AnimatedMediaDetailExpendedTopBar(
                        title = title,
                        show = state.isOptionsVisible,
                        onClose = navigateUp,
                    )
                }
            }
        },
        content = {
            MediaDetailPager(
                currentPage = state.selectedMediaItemIndex,
                pagerMediaItems = pagerMediaItems,
                onCurrentPageChanged = { pageIndex ->
                    onAction(MediaDetailScreenActions.OnSelectedIndexChanged(index = pageIndex))
                },
            ) { media ->
                MediaDetail(
                    media = media,
                    showFloatOptions = state.isOptionsVisible,
                    floatOptions = {
                        MediaOptions(
                            onShareMedia = {
                                onAction(MediaDetailScreenActions.OnShareMedia)
                            },
                            onDeleteMedia = {
                                media.uriString.trashMediaItemByUri(
                                    context = context,
                                    trashLauncher = trashLauncher,
                                )
                            },
                            extraActions = {
                                if (media.mimeType == MediaType.VIDEO) {
                                    IconButton(
                                        onClick = {
                                            onAction(MediaDetailScreenActions.OnPlayVideo)
                                        },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.PlayArrow,
                                            contentDescription = "Play Video",
                                        )
                                    }
                                }
                            },
                        )
                    },
                )
            }
        },
    )
}
