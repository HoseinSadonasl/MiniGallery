package com.hotaku.media.screens.media_detail

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val context = LocalContext.current

    val state by viewModel.mediaDetailUiState.collectAsStateWithLifecycle()
    val windowWidth = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

    val trashLauncher =
        rememberTrashLauncherForResult {
            onAction(MediaDetailScreenActions.OnDeleteMedia)
        }

    LaunchedEffect(viewModel.mediaDetailUiEvents) {
        viewModel.mediaDetailUiEvents.collectLatest { event ->
            when (event) {
                MediaDetailScreenEvents.OnViewMedia -> {
                    state.media[state.selectedMediaItemIndex].sendIntent(
                        context = context,
                        intentAction = Intent.ACTION_VIEW,
                    )
                }
                MediaDetailScreenEvents.OnShareMedia -> {
                    state.media[state.selectedMediaItemIndex].sendIntent(
                        context = context,
                        intentAction = Intent.ACTION_SEND,
                    )
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
            pagerMediaItems = state.media,
        ) { page, media ->
            SideEffect { onAction(MediaDetailScreenActions.OnPageChanged(page)) }
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
