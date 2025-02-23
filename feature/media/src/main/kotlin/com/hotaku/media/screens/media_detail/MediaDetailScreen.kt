package com.hotaku.media.screens.media_detail

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.media.components.MediaDetail
import com.hotaku.media.components.MediaOptions
import com.hotaku.media.components.MediaPreviewPager
import com.hotaku.media.utils.rememberTrashLauncherForResult
import com.hotaku.media.utils.sendIntent
import com.hotaku.media.utils.trashMediaItemByUri
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun MediaDetailScreen(
    modifier: Modifier = Modifier,
    mediaDetailViewModel: MediaDetailViewModel,
    selectedMediaItemIndex: Int,
    navigateUp: () -> Unit,
) {
    MediaDetailScreen(
        modifier = modifier,
        mediaDetailViewModel = mediaDetailViewModel,
        selectedMediaItemIndex = selectedMediaItemIndex,
        navigateUp = navigateUp,
        onAction = mediaDetailViewModel::onAction,
    )
}

@Composable
private fun MediaDetailScreen(
    modifier: Modifier = Modifier,
    mediaDetailViewModel: MediaDetailViewModel,
    selectedMediaItemIndex: Int,
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
                MediaDetailScreenEvents.OnViewMedia -> {
                    mediaListState.peek(state.selectedMediaItemIndex)?.sendIntent(
                        context = context,
                        intentAction = Intent.ACTION_VIEW,
                    )
                }
                MediaDetailScreenEvents.OnShareMedia -> {
                    mediaListState.peek(state.selectedMediaItemIndex)?.sendIntent(
                        context = context,
                        intentAction = Intent.ACTION_SEND,
                    )
                }
                MediaDetailScreenEvents.OnRefreshMedia -> {
                    mediaListState.refresh()
                }
            }
        }
    }

    Box(
        modifier = modifier,
    ) {
        MediaPreviewPager(
            modifier = Modifier,
            currentPage = selectedMediaItemIndex,
            pagerMediaItems = mediaListState.itemSnapshotList.items,
            onCurrentPageChanged = { pageIndex ->
                onAction(MediaDetailScreenActions.OnSelectedIndexChanged(index = pageIndex))
            },
        ) { page, media ->
            MediaDetail(
                isCompact = windowWidth == WindowWidthSizeClass.COMPACT,
                media = media,
                onPlayVideo = {
                    onAction(MediaDetailScreenActions.OnViewMedia)
                },
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
                    )
                },
            )
        }
    }
}
