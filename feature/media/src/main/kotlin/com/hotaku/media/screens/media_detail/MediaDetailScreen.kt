package com.hotaku.media.screens.media_detail

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.feature.media.R
import com.hotaku.media.components.MediaDetail
import com.hotaku.media.components.MediaOptions
import com.hotaku.media.components.MediaPreviewPager
import com.hotaku.media.utils.sendIntent
import com.hotaku.ui.conposables.AlertDialog
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

    state.mediaDetailDialog?.let { dialog ->
        when (dialog) {
            MediaDetailsDialogs.DeleteMediaDialog -> {
                AlertDialog(
                    title = stringResource(R.string.all_dialog_warning),
                    description = stringResource(R.string.delete_media_dialog_description_delete_this_media_file),
                    confirmButtonLabel = stringResource(R.string.delete_media_dialog_delete_button),
                    onDismiss = {
                        onAction(MediaDetailScreenActions.OnCloseDialog)
                    },
                    onConfirm = {
                        onAction(MediaDetailScreenActions.OnDeleteMedia)
                    },
                )
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
        ) { media ->
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
                            onAction(MediaDetailScreenActions.OnShowDeleteMediaDialog)
                        },
                    )
                },
            )
        }
    }
}
