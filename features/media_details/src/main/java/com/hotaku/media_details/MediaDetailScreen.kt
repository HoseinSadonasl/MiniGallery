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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.common.Logger
import com.hotaku.common.deleteMediaRequest
import com.hotaku.common.trashMediaRequest
import com.hotaku.common.writeMediaRequest
import com.hotaku.core_feature.ui.R
import com.hotaku.features.media_details.R.drawable.all_restore_trashed
import com.hotaku.features.media_details.R.string
import com.hotaku.media_details.MediaDetailScreenActions.OnClearMediaNameQuery
import com.hotaku.media_details.MediaDetailScreenActions.OnDeleteMedia
import com.hotaku.media_details.MediaDetailScreenActions.OnHideDialog
import com.hotaku.media_details.MediaDetailScreenActions.OnHideOptions
import com.hotaku.media_details.MediaDetailScreenActions.OnHideOptionsMenu
import com.hotaku.media_details.MediaDetailScreenActions.OnItemIsFavoriteChange
import com.hotaku.media_details.MediaDetailScreenActions.OnMediaNameChange
import com.hotaku.media_details.MediaDetailScreenActions.OnMediaNameQueryChange
import com.hotaku.media_details.MediaDetailScreenActions.OnRenameMediaItem
import com.hotaku.media_details.MediaDetailScreenActions.OnSelectedIndexChanged
import com.hotaku.media_details.MediaDetailScreenActions.OnShowDetails
import com.hotaku.media_details.MediaDetailScreenActions.OnShowOptions
import com.hotaku.media_details.MediaDetailScreenActions.OnShowOptionsMenu
import com.hotaku.media_details.MediaDetailScreenActions.OnShowRenameMediaDialog
import com.hotaku.media_details.MediaDetailScreenActions.OnTrashMedia
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

@Composable
fun MediaDetailScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
) {
    val context = LocalContext.current
    val mediaDetailViewModel = hiltViewModel<MediaDetailViewModel>()
    val state by mediaDetailViewModel.state.collectAsStateWithLifecycle()
    val pagingMediaItems = state.media.collectAsLazyPagingItems()

    MediaDetailScreenContent(
        modifier = modifier,
        context = context,
        state = state,
        media = pagingMediaItems,
        navigateUp = navigateUp,
        onAction = mediaDetailViewModel::onAction,
    )
}

@Composable
private fun MediaDetailScreenContent(
    modifier: Modifier = Modifier,
    context: Context,
    state: MediaDetailUiState,
    media: LazyPagingItems<MediaUi>,
    navigateUp: () -> Unit,
    onAction: (MediaDetailScreenActions) -> Unit,
) {
    val windowWidth = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

    val trashLauncher =
        rememberLauncherForStartIntentSenderForResult {
            state.selectedMediaIndex.let { index ->
                media.peek(index)?.let { media ->
                    onAction(
                        OnTrashMedia(
                            mediaItem = media,
                        ),
                    )
                }
            }
        }

    val deleteLauncher =
        rememberLauncherForStartIntentSenderForResult {
            state.selectedMediaIndex.let { index ->
                media.peek(index)?.let { media ->
                    onAction(
                        OnDeleteMedia(
                            mediaItem = media,
                        ),
                    )
                }
            }
        }

    val renameLauncher =
        rememberLauncherForStartIntentSenderForResult {
            state.selectedMediaIndex.let {
                media.peek(it)?.let { mediaItem ->
                    onAction(OnRenameMediaItem(media = mediaItem))
                }
            }
        }

    val favoriteLauncher =
        rememberLauncherForStartIntentSenderForResult {
            state.selectedMediaIndex.let {
                media.peek(it)?.let { mediaItem ->
                    onAction(OnItemIsFavoriteChange(mediaItem = mediaItem))
                }
            }
        }

    LaunchedEffect(
        key1 = state.isOptionsVisible,
        key2 = state.isOptionsMenuVisible,
    ) {
        if (state.isOptionsVisible && !state.isOptionsMenuVisible) {
            delay(1500)
            onAction(OnHideOptions)
        }
    }

    LaunchedEffect(
        key1 = media.itemCount,
        key2 = state.selectedMediaIndex,
    ) {
        if (media.itemCount > 0 && state.selectedMediaIndex < media.itemCount) {
            media.peek(state.selectedMediaIndex)?.displayName.orEmpty().let {
                onAction(OnMediaNameChange(mediaName = it))
            }
        } else {
            Logger.debugWarningLog(kClass = this@LaunchedEffect::class, message = "Media item count is 0")
        }
    }

    when (state.dialog) {
        MediaDialogs.RenameMediaDialog -> {
            RenameDialog(
                query = state.mediaNameQuery,
                onAction = onAction,
                mediaUriString = media.peek(state.selectedMediaIndex)?.uriString,
                context = context,
                renameLauncher = renameLauncher,
            )
        }
        MediaDialogs.Idle -> Unit
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
            when (media.loadState.append) {
                is LoadState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is LoadState.Error -> {
                    LoadMediaItemError()
                }

                else -> {}
            }
            when (media.loadState.refresh) {
                is LoadState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is LoadState.Error -> {
                    LoadMediaItemError()
                }

                else -> {
                    MediaDetailPager(
                        state = state,
                        pagingMediaItems = media,
                        onAction = onAction,
                        context = context,
                        trashLauncher = trashLauncher,
                        deleteLauncher = deleteLauncher,
                        favoriteLauncher = favoriteLauncher,
                    )
                }
            }
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
    deleteLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    favoriteLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
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
                            isFavorite = media.isFavorite,
                            onFavoriteMedia = {
                                media.uriString.writeMediaRequest(
                                    context = context,
                                    writeLauncher = favoriteLauncher,
                                )
                            },
                            onShareMedia = {
                                media.sendShareIntent(context = context)
                            },
                            onTrashMedia = {
                                if (state.matchTrash) {
                                    media.uriString.deleteMediaRequest(
                                        context = context,
                                        deleteLauncher = deleteLauncher,
                                    )
                                } else {
                                    media.uriString.trashMediaRequest(
                                        context = context,
                                        trashLauncher = trashLauncher,
                                    )
                                }
                            },
                            extraActions = {
                                if (media.mimeType == MediaType.VIDEO) {
                                    IconButton(
                                        onClick = {
                                            media.sendPlayIntent(context = context)
                                        },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.PlayArrow,
                                            contentDescription = "Play Video",
                                        )
                                    }
                                }
                                if (state.matchTrash) {
                                    IconButton(
                                        onClick = {
                                            media.uriString.trashMediaRequest(
                                                context = context,
                                                trashLauncher = trashLauncher,
                                                trash = false,
                                            )
                                        },
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = all_restore_trashed),
                                            contentDescription = "Restore trashed item",
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
        title = stringResource(R.string.input_dialog_title_rename),
        inputPlaceHolder = stringResource(R.string.input_dialog_placeholder_rename),
        inputValue = query,
        onInputChange = { value ->
            onAction(OnMediaNameQueryChange(query = value))
        },
        onConfirm = {
            onAction(OnHideDialog)
            mediaUriString?.writeMediaRequest(
                context = context,
                writeLauncher = renameLauncher,
            )
        },
        onDismissRequest = {
            onAction(OnHideDialog)
            onAction(OnClearMediaNameQuery)
        },
    )
}
