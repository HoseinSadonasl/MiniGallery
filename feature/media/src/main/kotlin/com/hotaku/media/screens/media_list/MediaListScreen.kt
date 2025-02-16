package com.hotaku.media.screens.media_list

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.feature.media.R
import com.hotaku.media.components.MediaGrid
import com.hotaku.media.components.MediaPreviewPager
import com.hotaku.media.components.MediaSyncLabel
import com.hotaku.media.components.OnScreenMessage
import com.hotaku.media.model.MediaUi
import com.hotaku.media.utils.shareMedia
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
    mediaViewModel: MediaViewModel,
    onShowSnackBar: suspend (String) -> Unit,
) {
    MediaListScreen(
        modifier = modifier,
        screenEvents = mediaViewModel.mediaScreenEvent,
        screenState = mediaViewModel.mediaScreenUiState,
        pagingMediaItemsState = mediaViewModel.mediaUiState,
        synchronizeState = mediaViewModel.synchronizeUiState,
        onAction = mediaViewModel::onAction,
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
    synchronizeState: StateFlow<UiState<Int>>,
    onAction: (MediaListScreenActions) -> Unit,
    onShowSnackBar: suspend (String) -> Unit,
) {
    val state: MediaListUiState by screenState.collectAsStateWithLifecycle()
    val synchronize: UiState<Int> by synchronizeState.collectAsStateWithLifecycle()
    val pagingMediaItems: LazyPagingItems<MediaUi> = pagingMediaItemsState.collectAsLazyPagingItems()

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    val windowWidth = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

    val navigator = rememberSupportingPaneScaffoldNavigator<Int>()

    val mediaPagerState =
        rememberPagerState(
            pageCount = { pagingMediaItems.itemCount },
        )

    BackHandler(navigator.canNavigateBack()) {
        onAction(MediaListScreenActions.OnClearSelectedMediaList)
    }

    BackHandler(state.isSearchExpanded) {
        focusManager.clearFocus()
        onAction(MediaListScreenActions.OnQueryChange(query = ""))
        onAction(MediaListScreenActions.OnCollepseSearch)
    }

    LaunchedEffect(state.query) {
        onAction(MediaListScreenActions.OnUpdateMediaList)
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
            navigator.navigateTo(ThreePaneScaffoldRole.Secondary, it)
            mediaPagerState.scrollToPage(it)
        }
    }

    LaunchedEffect(screenEvents) {
        screenEvents.collectLatest { event ->
            when (event) {
                MediaListScreenEvents.OnCloseMediaListPreview -> {
                    navigator.navigateBack()
                }
                MediaListScreenEvents.OnShareMediaList -> {
                    state.selectedMediaIndex?.let { pagingMediaItems[it]?.shareMedia(context = context) }
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
                                SyncSection(synchronize)
                            }
                            if (synchronize is UiState.Success && pagingMediaItems.itemCount == 0) {
                                NoMedia()
                            } else {
                                MediaGrid(
                                    modifier = Modifier.weight(1f),
                                    pagingMediaItems = pagingMediaItems,
                                    onScrolled = { scrolled ->
                                        onAction(MediaListScreenActions.OnSetTopBarVisibility(visible = !scrolled))
                                    },
                                    onItemClick = { itemIndex ->
                                        onAction(MediaListScreenActions.OnMediaListClick(itemIndex))
                                    },
                                    onItemLongClick = {
                                        onAction(MediaListScreenActions.OnMediaListLongClick)
                                    },
                                )
                            }
                        }
                    }
                },
                supportingPane = {
                    AnimatedPane {
                        navigator.currentDestination?.content?.let { media ->
                            MediaPreviewPager(
                                modifier =
                                    Modifier
                                        .then(
                                            if (!state.isTopBarVisible) Modifier.statusBarsPadding() else Modifier,
                                        ),
                                mediaPagerState = mediaPagerState,
                                isCompact = windowWidth == WindowWidthSizeClass.COMPACT,
                                pagingMediaItems = pagingMediaItems.itemSnapshotList.items,
                                onAction = onAction,
                            )
                        }
                    }
                },
            )
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
private fun SyncSection(synchronizeState: UiState<Int>) {
    when (synchronizeState) {
        is UiState.Failure -> {
            MediaSyncLabel(
                icon = Icons.Default.Warning,
                label = synchronizeState.error.asString(),
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
