package com.hotaku.media_details

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.ui.MediaType
import com.hotaku.ui.conposables.MediaDetail
import com.hotaku.ui.conposables.MediaOptions
import com.hotaku.ui.conposables.MediaPreviewPager
import com.hotaku.ui.rememberTrashLauncherForResult
import com.hotaku.ui.sendPlayIntent
import com.hotaku.ui.sendShareIntent
import com.hotaku.ui.trashMediaItemByUri
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

    val mediaListState = mediaDetailViewModel.mediaUiState.collectAsLazyPagingItems()

    val windowWidth = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

    val trashLauncher =
        rememberTrashLauncherForResult {
            state.selectedMediaItemIndex.let { index ->
                mediaListState.peek(index)?.let { media ->
                    onAction(
                        MediaDetailScreenActions.OnDeleteMedia(
                            mediaItem = media,
                        ),
                    )
                }
            }
        }

    LaunchedEffect(mediaDetailViewModel.mediaDetailUiEvents) {
        mediaDetailViewModel.mediaDetailUiEvents.collectLatest { event ->
            when (event) {
                MediaDetailScreenEvents.OnRefreshMedia -> {
                    mediaListState.refresh()
                }
                MediaDetailScreenEvents.OnShareMedia -> {
                    mediaListState.peek(state.selectedMediaItemIndex)?.sendShareIntent(context = context)
                }
                MediaDetailScreenEvents.OnPlayVideo -> {
                    mediaListState.peek(state.selectedMediaItemIndex)?.sendPlayIntent(context = context)
                }
            }
        }
    }

    Box(
        modifier = modifier,
    ) {
        MediaPreviewPager(
            modifier = Modifier,
            currentPage = state.selectedMediaItemIndex,
            pagerMediaItems = mediaListState,
            onCurrentPageChanged = { pageIndex ->
                onAction(MediaDetailScreenActions.OnSelectedIndexChanged(index = pageIndex))
            },
        ) { media ->
            MediaDetail(
                isCompact = windowWidth == WindowWidthSizeClass.COMPACT,
                media = media,
                onClose = navigateUp,
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
    }
}
