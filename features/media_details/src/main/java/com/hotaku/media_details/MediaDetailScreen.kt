package com.hotaku.media_details

import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.common.Logger
import com.hotaku.common.trashMediaRequest
import com.hotaku.common.writeMediaRequest
import com.hotaku.features.media_details.R.*
import com.hotaku.media_details.MediaDetailScreenActions.*
import com.hotaku.ui.MediaDialogs
import com.hotaku.ui.MediaOptionsMenuItems
import com.hotaku.ui.MediaType
import com.hotaku.ui.asString
import com.hotaku.ui.conposables.AnimatedMediaDetailCompactTopBar
import com.hotaku.ui.conposables.AnimatedMediaDetailExpendedTopBar
import com.hotaku.ui.conposables.InputDialog
import com.hotaku.ui.conposables.MediaDetail
import com.hotaku.ui.conposables.MediaDetailPager
import com.hotaku.ui.conposables.MediaDetailSurface
import com.hotaku.ui.conposables.MediaOptions
import com.hotaku.ui.conposables.OnScreenMessage
import com.hotaku.ui.conposables.OptionMenuItem
import com.hotaku.ui.conposables.OptionsMenu
import com.hotaku.ui.conposables.noRippleClickable
import com.hotaku.ui.models.MediaUi
import com.hotaku.ui.rememberLauncherForStartIntentSenderForResult
import com.hotaku.ui.sendPlayIntent
import com.hotaku.ui.sendShareIntent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MediaDetailScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
) {
    val mediaDetailViewModel = hiltViewModel<MediaDetailViewModel>()

    MediaDetailScreenContent(
        modifier = modifier,
        mediaDetailViewModel = mediaDetailViewModel,
        navigateUp = navigateUp,
        onAction = mediaDetailViewModel::onAction,
    )
}

@Composable
private fun MediaDetailScreenContent(
    modifier: Modifier = Modifier,
    mediaDetailViewModel: MediaDetailViewModel,
    navigateUp: () -> Unit,
    onAction: (MediaDetailScreenActions) -> Unit,
) {
    val context = LocalContext.current

    val state by mediaDetailViewModel.mediaDetailUiState.collectAsStateWithLifecycle()

    val pagingMediaItems = mediaDetailViewModel.mediaUiState.collectAsLazyPagingItems()

    val refreshLoadState = pagingMediaItems.loadState.refresh

    val windowWidth = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

    val trashLauncher =
        rememberLauncherForStartIntentSenderForResult {
            state.selectedMediaIndex.let { index ->
                pagingMediaItems.peek(index)?.let { media ->
                    onAction(
                        OnTrashMedia(
                            mediaItem = media,
                        ),
                    )
                }
            }
        }

    val renameLauncher =
        rememberLauncherForStartIntentSenderForResult {
            state.selectedMediaIndex.let {
                pagingMediaItems.peek(it)?.let { mediaItem ->
                    onAction(OnRenameMediaItem(media = mediaItem))
                }
            }
        }

    LaunchedEffect(state.isOptionsVisible, state.isOptionsMenuVisible) {
        if (state.isOptionsVisible && !state.isOptionsMenuVisible) {
            delay(1500)
            onAction(OnHideOptions)
        }
    }

    LaunchedEffect(mediaDetailViewModel.mediaDetailUiEvents) {
        mediaDetailViewModel.mediaDetailUiEvents.collectLatest { event ->
            when (event) {
                MediaDetailScreenEvents.OnRefreshMedia -> {
                    pagingMediaItems.refresh()
                }
                MediaDetailScreenEvents.OnShareMedia -> {
                    pagingMediaItems.peek(state.selectedMediaIndex)?.sendShareIntent(context = context)
                }
                MediaDetailScreenEvents.OnPlayVideo -> {
                    pagingMediaItems.peek(state.selectedMediaIndex)?.sendPlayIntent(context = context)
                }
            }
        }
    }

    when (state.dialog) {
        MediaDialogs.RenameMediaDialog -> {
            RenameDialog(
                query = state.mediaNameQuery,
                onAction = onAction,
                mediaUriString = pagingMediaItems.peek(state.selectedMediaIndex)?.uriString,
                context = context,
                renameLauncher = renameLauncher,
            )
        }
        MediaDialogs.Idle -> Unit
    }

    LaunchedEffect(
        key1 = pagingMediaItems.itemCount,
        key2 = state.selectedMediaIndex,
    ) {
        if (pagingMediaItems.itemCount > 0 && state.selectedMediaIndex < pagingMediaItems.itemCount) {
            pagingMediaItems.peek(state.selectedMediaIndex)?.displayName.orEmpty().let {
                onAction(OnMediaNameChange(mediaName = it))
            }
        } else {
            Logger.debugWarningLog(kClass = this@LaunchedEffect::class, message = "Media item count is 0")
        }
    }

    MediaDetailSurface(
        modifier =
            modifier
                .noRippleClickable {
                    onAction(OnShowOptions)
                },
        topContent = {
            Box(modifier = Modifier.statusBarsPadding()) {
                if (windowWidth == WindowWidthSizeClass.COMPACT) {
                    AnimatedMediaDetailCompactTopBar(
                        title = state.mediaName,
                        show = state.isOptionsVisible,
                        onClose = navigateUp,
                    )
                } else {
                    AnimatedMediaDetailExpendedTopBar(
                        title = state.mediaName,
                        show = state.isOptionsVisible,
                        onClose = navigateUp,
                    )
                }
            }
        },
        content = {
            when (refreshLoadState) {
                is LoadState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is LoadState.Error -> {
                    LoadMediaItemError()
                }

                else -> Unit
            }

            MediaDetailPager(
                state = state,
                pagingMediaItems = pagingMediaItems,
                onAction = onAction,
                context = context,
                trashLauncher = trashLauncher,
            )
        },
    )
}

@Composable
private fun LoadMediaItemError() {
    OnScreenMessage(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.error,
        title = stringResource(string.media_detail_screen_error_loading_media_title),
        fulMessage = stringResource(string.media_detail_screen_error_loading_media_message),
    )
}

@Composable
private fun MediaDetailPager(
    state: MediaDetailUiState,
    pagingMediaItems: LazyPagingItems<MediaUi>,
    onAction: (MediaDetailScreenActions) -> Unit,
    context: Context,
    trashLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
) {
    MediaDetailPager(
        currentPage = state.selectedMediaIndex,
        pagerMediaItems = pagingMediaItems,
        onCurrentPageChanged = { pageIndex ->
            onAction(OnSelectedIndexChanged(index = pageIndex))
        },
    ) { media ->
        MediaDetail(
            media = media,
            showFloatOptions = state.isOptionsVisible,
            floatOptions = {
                OptionsMenu(
                    expend = state.isOptionsMenuVisible,
                    node = {
                        MediaOptions(
                            onShareMedia = {
                                onAction(OnShareMedia)
                            },
                            onTrashMedia = {
                                media.uriString.trashMediaRequest(
                                    context = context,
                                    trashLauncher = trashLauncher,
                                )
                            },
                            extraActions = {
                                if (media.mimeType == MediaType.VIDEO) {
                                    IconButton(
                                        onClick = {
                                            onAction(OnPlayVideo)
                                        },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.PlayArrow,
                                            contentDescription = "Play Video",
                                        )
                                    }
                                }
                            },
                            moreAction = {
                                onAction(OnShowOptionsMenu)
                            },
                        )
                    },
                    options = {
                        MediaOptionsMenuItems.entries.forEach { item ->
                            OptionMenuItem(
                                option = item.text.asString(),
                            ) {
                                when (item) {
                                    MediaOptionsMenuItems.RENAME -> {
                                        onAction(
                                            OnShowRenameMediaDialog,
                                        )
                                    }

                                    MediaOptionsMenuItems.DETAILS -> {
                                        onAction(
                                            OnShowDetails,
                                        )
                                    }
                                }
                            }
                        }
                    },
                    onDismissRequest = {
                        onAction(
                            OnHideOptionsMenu,
                        )
                    },
                )
            },
        )
    }
}

@Composable
private fun RenameDialog(
    query: String,
    onAction: (MediaDetailScreenActions) -> Unit,
    mediaUriString: String?,
    context: Context,
    renameLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
) {
    InputDialog(
        title = stringResource(com.hotaku.core_feature.ui.R.string.input_dialog_title_rename),
        inputPlaceHolder = stringResource(com.hotaku.core_feature.ui.R.string.input_dialog_placeholder_rename),
        inputValue = query,
        onInputChange = { value ->
            onAction(OnMediaNameQueryChange(query = value))
        },
        onConfirm = {
            onAction(OnHideDialog)
            mediaUriString?.writeMediaRequest(
                context = context,
                trashLauncher = renameLauncher,
            )
        },
        onDismissRequest = {
            onAction(OnHideDialog)
            onAction(OnClearMediaNameQuery)
        },
    )
}
