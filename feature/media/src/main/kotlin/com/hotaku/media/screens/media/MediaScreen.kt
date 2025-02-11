package com.hotaku.media.screens.media

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.feature.media.R
import com.hotaku.media.components.MediaGrid
import com.hotaku.media.components.MediaSyncLabel
import com.hotaku.media.components.OnScreenMessage
import com.hotaku.media.model.MediaUi
import com.hotaku.ui.UiState
import com.hotaku.ui.asString
import com.hotaku.ui.conposables.AnimatedSearchTextField
import com.hotaku.ui.conposables.DynamicTopAppBarColumn
import com.hotaku.ui.conposables.TopAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

@Composable
internal fun MediaScreen(
    modifier: Modifier = Modifier,
    mediaViewModel: MediaViewModel,
    onShowSnackBar: suspend (String) -> Unit,
) {
    MediaScreen(
        modifier = modifier,
        screenState = mediaViewModel.mediaScreenUiState,
        pagingMediaItemsState = mediaViewModel.mediaUiState,
        synchronizeState = mediaViewModel.synchronizeUiState,
        onAction = mediaViewModel::onAction,
        onShowSnackBar = { onShowSnackBar(it) },
    )
}

@Composable
private fun MediaScreen(
    modifier: Modifier = Modifier,
    screenState: StateFlow<MediaUiState>,
    pagingMediaItemsState: StateFlow<PagingData<MediaUi>>,
    synchronizeState: StateFlow<UiState<Int>>,
    onAction: (MediaScreenActions) -> Unit,
    onShowSnackBar: suspend (String) -> Unit,
) {
    val state: MediaUiState by screenState.collectAsStateWithLifecycle()
    val synchronize: UiState<Int> by synchronizeState.collectAsStateWithLifecycle()
    val pagingMediaItems: LazyPagingItems<MediaUi> = pagingMediaItemsState.collectAsLazyPagingItems()

    val focusManager = LocalFocusManager.current

    BackHandler(state.isSearchExpanded) {
        focusManager.clearFocus()
        onAction(MediaScreenActions.OnQueryChange(query = ""))
        onAction(MediaScreenActions.OnCollepseSearch)
    }

    BackHandler(state.selectedAlbum != null) {
        onAction(MediaScreenActions.OnClearSelectedAlbum)
    }

    LaunchedEffect(state.query) {
        onAction(MediaScreenActions.OnUpdateMedia)
    }

    LaunchedEffect(synchronize) {
        if (synchronize is UiState.Success) {
            pagingMediaItems.refresh()
            delay(3000)
            onAction(MediaScreenActions.OnHideSyncSection)
        }
    }

    DynamicTopAppBarColumn(
        modifier = modifier,
        show = !state.isScrolled,
        animatableTopContent = {
            TopAppBar(
                title = stringResource(R.string.home_screentop_bar_title_all_media),
                content = {
                    AnimatedSearchTextField(
                        expanded = state.isSearchExpanded,
                        onIconClick = {
                            if (state.isSearchExpanded && state.query.isEmpty()) {
                                onAction(MediaScreenActions.OnCollepseSearch)
                            } else {
                                onAction(MediaScreenActions.OnExpandSearch)
                            }
                        },
                        value = state.query,
                        onValueChange = { query ->
                            onAction(MediaScreenActions.OnQueryChange(query = query))
                        },
                        placeHolderText = stringResource(R.string.home_screen_search_media),
                    )
                },
            )
        },
        content = {
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
                        onAction(MediaScreenActions.OnScrolled(isScrolled = scrolled))
                    },
                    onItemClick = { item ->
                        onAction(MediaScreenActions.OnMediaClick(item))
                    },
                    onItemLongClick = {
                        onAction(MediaScreenActions.OnMediaLongClick)
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
