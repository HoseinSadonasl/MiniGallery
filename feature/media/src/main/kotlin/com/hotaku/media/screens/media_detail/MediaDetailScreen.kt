package com.hotaku.media.screens.media_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.media.components.MediaDetail
import com.hotaku.media.components.MediaOptions
import com.hotaku.media.components.MediaPreviewPager

@Composable
internal fun MediaDetailScreen(
    modifier: Modifier = Modifier,
    mediaDetailViewModel: MediaDetailViewModel,
    navigateUp: () -> Unit,
) {
    MediaDetailScreen(
        modifier = modifier,
        viewModel = mediaDetailViewModel,
        navigateUp = navigateUp,
        onAction = mediaDetailViewModel::onAction,
    )
}

@Composable
private fun MediaDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MediaDetailViewModel,
    navigateUp: () -> Unit,
    onAction: (MediaDetailScreenActions) -> Unit,
) {
    val state by viewModel.mediaDetailUiState.collectAsStateWithLifecycle()

    val windowWidth = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

    Box(
        modifier = modifier,
    ) {
        MediaPreviewPager(
            modifier = Modifier,
            currentPage = state.selectedMediaItemIndex,
            pagerMediaItems = state.media,
        ) { media ->
            MediaDetail(
                isCompact = windowWidth == WindowWidthSizeClass.COMPACT,
                media = media,
                onPlayVideo = {
                    // play video
                },
                onClose = navigateUp,
                floatOptions = {
                    MediaOptions(
                        onShareMedia = {
                            // share media
                        },
                        onDeleteMedia = {
                            onAction(MediaDetailScreenActions.OnDeleteMedia)
                        },
                    )
                },
            )
        }
    }
}
