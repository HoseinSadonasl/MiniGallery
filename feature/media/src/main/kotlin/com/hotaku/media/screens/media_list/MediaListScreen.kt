package com.hotaku.media.screens.media_list

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.feature.media.R
import com.hotaku.media.components.MediaDetail
import com.hotaku.media.components.MediaGrid
import com.hotaku.media.components.MediaOptions
import com.hotaku.media.components.MediaPreviewPager
import com.hotaku.media.components.MediaSyncLabel
import com.hotaku.media.components.OnScreenMessage
import com.hotaku.media.model.MediaUi
import com.hotaku.media.utils.rememberTrashLauncherForResult
import com.hotaku.media.utils.sendIntent
import com.hotaku.media.utils.trashMediaItemByUri
import com.hotaku.ui.UiState
import com.hotaku.ui.asString
import com.hotaku.ui.conposables.AnimatedSearchTextField
import com.hotaku.ui.conposables.DynamicTopAppBarColumn
import com.hotaku.ui.conposables.TopAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun MediaListScreen(
    modifier: Modifier = Modifier,
    mediaListViewModel: MediaListViewModel,
    navigateToMediaDetailScreen: () -> Unit,
    onShowSnackBar: suspend (String) -> Unit,
) {
    MediaListScreen(
        modifier = modifier,
        screenEvents = mediaListViewModel.mediaScreenEvent,
        screenState = mediaListViewModel.mediaListScreenUiState,
        pagingMediaItemsState = mediaListViewModel.mediaUiState,
        synchronizeState = mediaListViewModel.synchronizeUiState,
        onAction = mediaListViewModel::onAction,
        navigateToMediaDetailScreen = navigateToMediaDetailScreen,
        onShowSnackBar = { onShowSnackBar(it) },
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
    navigateToMediaDetailScreen: () -> Unit,
    onAction: (MediaListScreenActions) -> Unit,
    onShowSnackBar: suspend (String) -> Unit,
) {
    val state: MediaListUiState by screenState.collectAsStateWithLifecycle()
    val synchronize: UiState<Int>? by synchronizeState.collectAsStateWithLifecycle()
    val pagingMediaItems: LazyPagingItems<MediaUi> =
        pagingMediaItemsState.collectAsLazyPagingItems()

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    val windowWidth = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

    val navigator = rememberSupportingPaneScaffoldNavigator<Int>()

    val trashLauncher =
        rememberTrashLauncherForResult {
            state.selectedMediaIndex?.let {
                pagingMediaItems.peek(it)?.let { mediaItem ->
                    onAction(MediaListScreenActions.OnDeleteMediaItem(mediaItem = mediaItem))
                }
            }
        }

    BackHandler(navigator.canNavigateBack()) {
        onAction(MediaListScreenActions.OnClearSelectedMedia)
    }

    BackHandler(state.isSearchExpanded) {
        focusManager.clearFocus()
        onAction(MediaListScreenActions.OnQueryChange(query = ""))
        onAction(MediaListScreenActions.OnCollepseSearch)
    }

    LaunchedEffect(synchronize) {
        if (synchronize is UiState.Success) {
            pagingMediaItems.refresh()
            delay(3000)
            onAction(MediaListScreenActions.OnHideSyncSection)
        }
    }

    LaunchedEffect(state.selectedMediaIndex) {
        state.selectedMediaIndex?.let {
            onAction(MediaListScreenActions.OnSetTopBarVisibility(visible = false))
            if (windowWidth == WindowWidthSizeClass.COMPACT) {
                navigateToMediaDetailScreen()
                onAction(MediaListScreenActions.OnClearSelectedMedia)
            } else {
                navigator.navigateTo(ThreePaneScaffoldRole.Secondary, it)
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
                        pagingMediaItems[it]?.sendIntent(
                            context = context,
                            intentAction = Intent.ACTION_SEND,
                        )
                    }
                }

                MediaListScreenEvents.OnNavigateToMediaDetail -> {
                    navigateToMediaDetailScreen()
                }

                MediaListScreenEvents.OnRefreshList -> {
                    pagingMediaItems.refresh()
                }
            }
        }
    }

    DynamicTopAppBarColumn(
        modifier = modifier,
        show = state.isTopBarVisible,
        animatableTopContent = {
            TopAppBar(
                title = stringResource(R.string.home_screentop_bar_title_all_media),
                content = {
                    AnimatedSearchTextField(
                        expanded = state.isSearchExpanded,
                        onIconClick = {
                            if (state.isSearchExpanded && state.query.isEmpty()) {
                                onAction(MediaListScreenActions.OnCollepseSearch)
                            } else {
                                onAction(MediaListScreenActions.OnExpandSearch)
                            }
                        },
                        value = state.query,
                        onValueChange = { query ->
                            onAction(MediaListScreenActions.OnQueryChange(query = query))
                        },
                        placeHolderText = stringResource(R.string.home_screen_search_media),
                    )
                },
            )
        },
        content = {
            if (synchronize is UiState.Success && pagingMediaItems.itemCount == 0) {
                NoMedia()
            } else {
                SupportingPaneScaffold(
                    directive = navigator.scaffoldDirective,
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
                                MediaGrid(
                                    modifier = Modifier.weight(1f),
                                    pagingMediaItems = pagingMediaItems,
                                    onScrolled = { scrolled ->
                                        onAction(
                                            MediaListScreenActions.OnSetTopBarVisibility(
                                                visible = !scrolled,
                                            ),
                                        )
                                    },
                                    onItemClick = { itemIndex ->
                                        onAction(MediaListScreenActions.OnMediaListItemClick(itemIndex))
                                    },
                                    onItemLongClick = {
                                        onAction(MediaListScreenActions.OnMediaListItemLongClick)
                                    },
                                )
                            }
                        }
                    },
                    supportingPane = {
                        AnimatedPane {
                            navigator.currentDestination?.content?.let { index ->
                                MediaPreviewPager(
                                    modifier = Modifier,
                                    currentPage = index,
                                    pagerMediaItems = pagingMediaItems.itemSnapshotList.items,
                                    onCurrentPageChanged = { currentIndex ->
                                        onAction(MediaListScreenActions.OnMediaListItemClick(mediaItemIndex = currentIndex))
                                    },
                                ) { pageIndex, media ->
                                    MediaDetail(
                                        isCompact = windowWidth == WindowWidthSizeClass.COMPACT,
                                        media = media,
                                        onPlayVideo = {
                                            // play video
                                        },
                                        onClose = {
                                            onAction(MediaListScreenActions.OnClearSelectedMedia)
                                        },
                                        floatOptions = {
                                            MediaOptions(
                                                onShareMedia = {
                                                    onAction(MediaListScreenActions.OnShareMedia)
                                                },
                                                onDeleteMedia = {
                                                    media.uriString.trashMediaItemByUri(
                                                        context = context,
                                                        trashLauncher = trashLauncher,
                                                    )
                                                },
                                                extraActions = {
                                                    IconButton(
                                                        onClick = {
                                                            onAction(MediaListScreenActions.OnOpenMedia)
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
                                                },
                                            )
                                        },
                                    )
                                }
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
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        OnScreenMessage(
            title = stringResource(id = R.string.home_screen_no_media),
            fulMessage = stringResource(id = R.string.home_screen_no_media_full_message),
        )
    }
}

@Preview(showBackground = true)
@PreviewScreenSizes
@Composable
private fun NoMediaPreview() {
    MiniGalleryTheme {
        NoMedia()
    }
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
                onRetry = { onAction(MediaListScreenActions.OnRetrySynchronizeMedia) },
            )
        }

        is UiState.Loading -> {
            MediaSyncLabel(
                isSyncing = true,
                label = stringResource(R.string.home_screen_state_synchronizing),
            )
        }

        is UiState.Success -> {
            MediaSyncLabel(
                icon = Icons.Default.Done,
                label =
                    stringResource(
                        R.string.home_screen_media_sync_state_media_added,
                        synchronizeState.data,
                    ),
            )
        }
    }
}
