package com.hotaku.media

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.common.Logger
import com.hotaku.common.trashMediaRequest
import com.hotaku.common.writeMediaRequest
import com.hotaku.features.media.R
import com.hotaku.media.MediaListScreenActions.*
import com.hotaku.media.components.MediaSyncLabel
import com.hotaku.ui.MediaDialogs.Idle
import com.hotaku.ui.MediaDialogs.RenameMediaDialog
import com.hotaku.ui.MediaOptionsMenuItems
import com.hotaku.ui.MediaType
import com.hotaku.ui.PermissionUtils
import com.hotaku.ui.PermissionUtils.requiredMediaPermissions
import com.hotaku.ui.UiState
import com.hotaku.ui.asString
import com.hotaku.ui.conposables.AnimatedMediaDetailCompactTopBar
import com.hotaku.ui.conposables.AnimatedMediaDetailExpendedTopBar
import com.hotaku.ui.conposables.DynamicTopAppBarColumn
import com.hotaku.ui.conposables.EmptyPaneMessage
import com.hotaku.ui.conposables.InputDialog
import com.hotaku.ui.conposables.MediaDetail
import com.hotaku.ui.conposables.MediaDetailPager
import com.hotaku.ui.conposables.MediaDetailSurface
import com.hotaku.ui.conposables.MediaGrid
import com.hotaku.ui.conposables.MediaOptions
import com.hotaku.ui.conposables.OnScreenMessage
import com.hotaku.ui.conposables.OptionMenuItem
import com.hotaku.ui.conposables.OptionsMenu
import com.hotaku.ui.conposables.TextField
import com.hotaku.ui.conposables.TopAppBar
import com.hotaku.ui.conposables.noRippleClickable
import com.hotaku.ui.models.MediaUi
import com.hotaku.ui.rememberLauncherForStartIntentSenderForResult
import com.hotaku.ui.sendPlayIntent
import com.hotaku.ui.sendShareIntent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MediaListScreen(
    modifier: Modifier = Modifier,
    navigateToMediaDetailScreen: (Int?) -> Unit,
    navigateToOnboardingScreen: () -> Unit,
) {
    val mediaListViewModel = hiltViewModel<MediaListViewModel>()
    MediaListScreen(
        modifier = modifier,
        screenEvents = mediaListViewModel.mediaScreenEvent,
        screenState = mediaListViewModel.mediaListScreenUiState,
        pagingMediaItemsState = mediaListViewModel.mediaUiState,
        synchronizeState = mediaListViewModel.synchronizeUiState,
        onAction = mediaListViewModel::onAction,
        navigateToMediaDetailScreen = navigateToMediaDetailScreen,
        navigateToOnboardingScreen = navigateToOnboardingScreen,
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun MediaListScreen(
    modifier: Modifier = Modifier,
    screenEvents: Flow<MediaListScreenEvents>,
    screenState: StateFlow<MediaListUiState>,
    pagingMediaItemsState: StateFlow<PagingData<MediaUi>>,
    synchronizeState: StateFlow<UiState<Int>?>,
    navigateToMediaDetailScreen: (Int?) -> Unit,
    onAction: (MediaListScreenActions) -> Unit,
    navigateToOnboardingScreen: () -> Unit,
) {
    val state: MediaListUiState by screenState.collectAsStateWithLifecycle()

    val synchronize: UiState<Int>? by synchronizeState.collectAsStateWithLifecycle()

    val pagingMediaItems: LazyPagingItems<MediaUi> = pagingMediaItemsState.collectAsLazyPagingItems()

    val refreshState = pagingMediaItems.loadState.refresh

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    val windowWidth = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

    val navigator = rememberSupportingPaneScaffoldNavigator<Int>()

    val trashLauncher =
        rememberLauncherForStartIntentSenderForResult {
            state.selectedMediaIndex?.let {
                pagingMediaItems.peek(it)?.let { mediaItem ->
                    onAction(OnTrashMediaItem(mediaItem = mediaItem))
                }
            }
        }

    val renameLauncher =
        rememberLauncherForStartIntentSenderForResult {
            state.selectedMediaIndex?.let {
                pagingMediaItems.peek(it)?.let { mediaItem ->
                    onAction(OnRenameMediaItem(media = mediaItem))
                }
            }
        }

    BackHandler(navigator.canNavigateBack()) {
        onAction(OnClearSelectedMedia)
    }

    BackHandler(state.isSearchFocused) {
        onAction(OnSearchQueryChange(query = ""))
        focusManager.clearFocus()
    }

    LaunchedEffect(Unit) {
        PermissionUtils.permissionsToRequest(
            context = context,
            permissions = requiredMediaPermissions,
        ).let { permissions ->
            if (permissions.isNotEmpty()) navigateToOnboardingScreen()
        }
    }

    LaunchedEffect(synchronize) {
        if (synchronize is UiState.Success) {
            pagingMediaItems.refresh()
            delay(3000)
            onAction(OnHideSyncSection)
        }
    }

    LaunchedEffect(state.query) {
        onAction(OnUpdateUpdateMedia)
    }

    LaunchedEffect(state.selectedMediaIndex) {
        state.selectedMediaIndex?.let {
            onAction(OnSetTopBarVisibility(visible = false))
            if (windowWidth == WindowWidthSizeClass.COMPACT) {
                navigateToMediaDetailScreen(state.selectedMediaIndex)
                onAction(OnClearSelectedMedia)
            } else {
                navigator.navigateTo(ThreePaneScaffoldRole.Secondary, it)
            }
        }
    }

    LaunchedEffect(
        key1 = pagingMediaItems.itemCount,
        key2 = state.selectedMediaIndex,
    ) {
        state.selectedMediaIndex?.let { index ->
            if (pagingMediaItems.itemCount > 0 && index > +0 && index < pagingMediaItems.itemCount) {
                pagingMediaItems.peek(index)?.displayName.orEmpty().let {
                    onAction(OnSelectedMediaNameChange(mediaName = it))
                }
            } else {
                Logger.debugWarningLog(
                    kClass = this@LaunchedEffect::class,
                    message = "Media item count is 0",
                )
            }
        }
    }

    LaunchedEffect(screenEvents) {
        screenEvents.collectLatest { event ->
            when (event) {
                MediaListScreenEvents.OnCloseMediaListPreview -> {
                    navigator.navigateBack()
                }

                MediaListScreenEvents.OnShareMediaList -> {
                    state.selectedMediaIndex?.let {
                        pagingMediaItems[it]?.sendShareIntent(context = context)
                    }
                }

                MediaListScreenEvents.OnNavigateToMediaDetail -> {
                    navigateToMediaDetailScreen(state.selectedMediaIndex)
                }

                MediaListScreenEvents.OnRefreshList -> {
                    pagingMediaItems.refresh()
                }

                MediaListScreenEvents.OnPlayVideo -> {
                    state.selectedMediaIndex?.let {
                        pagingMediaItems[it]?.sendPlayIntent(context = context)
                    }
                }
            }
        }
    }

    when (state.dialog) {
        RenameMediaDialog -> {
            state.selectedMediaIndex?.let { index ->
                RenameDialog(
                    query = state.mediaNameQuery,
                    onAction = onAction,
                    mediaUriString = pagingMediaItems.peek(index)?.uriString,
                    context = context,
                    renameLauncher = renameLauncher,
                )
            }
        }
        Idle -> Unit
    }

    DynamicTopAppBarColumn(
        modifier = modifier,
        show = state.isTopBarVisible,
        animatableTopContent = {
            TopAppBar(
                title = stringResource(R.string.media_list_screen_top_bar_title_all_media),
                content = {
                    TextField(
                        modifier =
                            Modifier.fillMaxWidth().onFocusChanged {
                                onAction(OnSearchFocusChanged(hasFocus = it.hasFocus))
                            },
                        value = state.query,
                        onValueChange = { query ->
                            onAction(OnSearchQueryChange(query = query))
                        },
                        placeHolderText = stringResource(R.string.media_list_screen_search_media),
                        endIcon = Icons.Outlined.Search,
                    )
                },
            )
        },
        content = {
            if (synchronize is UiState.Success && pagingMediaItems.itemCount == 0) {
                NoMedia()
            } else {
                SupportingPaneScaffold(
                    directive =
                        navigator.scaffoldDirective.copy(
                            horizontalPartitionSpacerSize = 8.dp,
                        ),
                    value = navigator.scaffoldValue,
                    mainPane = {
                        AnimatedPane {
                            Column(
                                Modifier.fillMaxSize(),
                            ) {
                                AnimatedVisibility(
                                    visible = state.showSyncSection,
                                ) {
                                    synchronize?.let { SyncSection(synchronizeState = it, onAction = onAction) }
                                }
                                when (refreshState) {
                                    is LoadState.Error -> {
                                        LoadMediaError(onAction = onAction)
                                    }
                                    LoadState.Loading -> {
                                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                    else -> Unit
                                }
                                MediaGrid(
                                    modifier = Modifier.weight(1f),
                                    pagingMediaItems = pagingMediaItems,
                                    onScrolled = { scrolled ->
                                        onAction(
                                            OnSetTopBarVisibility(
                                                visible = !scrolled,
                                            ),
                                        )
                                    },
                                    onItemClick = { itemIndex ->
                                        onAction(
                                            OnMediaListItemClick(
                                                itemIndex,
                                            ),
                                        )
                                    },
                                    onItemLongClick = {
                                        onAction(OnMediaListItemLongClick)
                                    },
                                )
                            }
                        }
                    },
                    supportingPane = {
                        AnimatedPane {
                            navigator.currentDestination?.content?.let { mediaItemIndex ->
                                SupportingPaneContent(
                                    windowWidth = windowWidth,
                                    pagingMediaItems = pagingMediaItems,
                                    state = state,
                                    onAction = onAction,
                                    index = mediaItemIndex,
                                    context = context,
                                    trashLauncher = trashLauncher,
                                )
                            } ?: Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                EmptyPaneMessage(
                                    icon = Icons.Outlined.Info,
                                    message = stringResource(R.string.media_list_screen_empty_pane_message),
                                )
                            }
                        }
                    },
                )
            }
        },
    )
}

@Composable
private fun NoMedia() {
    OnScreenMessage(
        modifier = Modifier.fillMaxSize(),
        title = stringResource(id = R.string.media_list_screen_no_media),
        fulMessage = stringResource(id = R.string.media_list_screen_no_media_full_message),
    )
}

@Composable
private fun LoadMediaError(onAction: (MediaListScreenActions) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        OnScreenMessage(
            modifier = Modifier.fillMaxSize(),
            title = stringResource(id = R.string.media_list_screen_error_loading_media_message),
            fulMessage = stringResource(id = R.string.media_list_screen_error_loading_media_full_message),
        )
        FilledTonalButton(
            onClick = {
                onAction(OnUpdateUpdateMedia)
            },
        ) {
            Text(
                text = stringResource(R.string.media_list_screen_error_loading_media_button_try_again),
            )
        }
    }
}

@Composable
private fun RenameDialog(
    query: String,
    onAction: (MediaListScreenActions) -> Unit,
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
            onAction(OnHideDiaDialog)
            mediaUriString?.writeMediaRequest(
                context = context,
                trashLauncher = renameLauncher,
            )
        },
        onDismissRequest = {
            onAction(OnHideDiaDialog)
            onAction(OnMediaNameClearQuery)
        },
    )
}

@Composable
private fun SupportingPaneContent(
    windowWidth: WindowWidthSizeClass,
    pagingMediaItems: LazyPagingItems<MediaUi>,
    state: MediaListUiState,
    onAction: (MediaListScreenActions) -> Unit,
    index: Int,
    context: Context,
    trashLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
) {
    LaunchedEffect(state.isOptionsVisible, state.isOptionsMenuVisible) {
        if (state.isOptionsVisible && !state.isOptionsMenuVisible) {
            delay(1500)
            onAction(OnHideOptions)
        }
    }

    MediaDetailSurface(
        modifier =
            Modifier
                .fillMaxSize()
                .noRippleClickable {
                    onAction(OnShowOptions)
                },
        topContent = {
            Box(
                modifier = Modifier.then(if (!state.isTopBarVisible) Modifier.statusBarsPadding() else Modifier),
            ) {
                if (windowWidth == WindowWidthSizeClass.COMPACT) {
                    AnimatedMediaDetailCompactTopBar(
                        title = state.selectedItemName,
                        show = state.isOptionsVisible,
                        onClose = {
                            onAction(OnClearSelectedMedia)
                        },
                    )
                } else {
                    AnimatedMediaDetailExpendedTopBar(
                        title = state.selectedItemName,
                        show = state.isOptionsVisible,
                        onClose = {
                            onAction(OnClearSelectedMedia)
                        },
                    )
                }
            }
        },
        content = {
            MediaDetailPager(
                modifier = Modifier,
                currentPage = index,
                pagerMediaItems = pagingMediaItems,
                onCurrentPageChanged = { currentIndex ->
                    onAction(
                        OnMediaListItemClick(
                            mediaItemIndex = currentIndex,
                        ),
                    )
                },
            ) { media ->
                MediaDetail(
                    showFloatOptions = state.isOptionsVisible,
                    media = media,
                    floatOptions = {
                        OptionsMenu(
                            expend = state.isOptionsMenuVisible,
                            node = {
                                MediaOptions(
                                    onShareMedia = {
                                        onAction(
                                            OnShareMedia,
                                        )
                                    },
                                    onTrashMedia = {
                                        media.uriString.trashMediaRequest(
                                            context = context,
                                            trashLauncher = trashLauncher,
                                        )
                                    },
                                    extraActions = {
                                        when (media.mimeType) {
                                            MediaType.VIDEO -> {
                                                IconButton(
                                                    onClick = {
                                                        onAction(
                                                            OnPlayVideo,
                                                        )
                                                    },
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.PlayArrow,
                                                        contentDescription = "Play video",
                                                    )
                                                }
                                            }

                                            MediaType.IMAGE -> {
                                                IconButton(
                                                    onClick = {
                                                        onAction(
                                                            OnOpenMediaDetails,
                                                        )
                                                    },
                                                ) {
                                                    Icon(
                                                        imageVector =
                                                            ImageVector.vectorResource(
                                                                id = R.drawable.media_preview_full_screen,
                                                            ),
                                                        contentDescription = "Open full screen",
                                                    )
                                                }
                                            }

                                            MediaType.UNKNOWN -> Unit
                                        }
                                    },
                                    moreAction = {
                                        onAction(
                                            OnShowOptionsMenu,
                                        )
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
                                                    OnOpenRenameMediaDialog,
                                                )
                                            }

                                            MediaOptionsMenuItems.DETAILS -> {
                                                onAction(
                                                    ShowDetails,
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                            onDismissRequest = {
                                onAction(OnHideOptionsMenu)
                            },
                        )
                    },
                )
            }
        },
    )
}

@Composable
private fun SyncSection(
    synchronizeState: UiState<Int>,
    onAction: (MediaListScreenActions) -> Unit,
) {
    when (synchronizeState) {
        is UiState.Failure -> {
            MediaSyncLabel(
                icon = Icons.Default.Warning,
                label = synchronizeState.error.asString(),
                onRetry = { onAction(OnRetrySynchronizeMedia) },
            )
        }

        is UiState.Loading -> {
            MediaSyncLabel(
                isSyncing = true,
                label = stringResource(R.string.media_list_screen_state_synchronizing),
            )
        }

        is UiState.Success -> {
            MediaSyncLabel(
                icon = Icons.Default.Done,
                label =
                    stringResource(
                        R.string.media_list_screen_media_sync_state_media_added,
                        synchronizeState.data,
                    ),
            )
        }
    }
}
