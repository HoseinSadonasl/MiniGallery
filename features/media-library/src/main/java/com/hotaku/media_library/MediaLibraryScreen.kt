package com.hotaku.media_library

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.features.media_library.R
import com.hotaku.media_library.MediaLibraryScreenActions.*
import com.hotaku.media_library.composables.HorizontalLibraryFolders
import com.hotaku.media_library.composables.LibraryFolderItem
import com.hotaku.media_library.composables.VerticalLibraryFolders
import com.hotaku.media_library.utils.LibraryItemsEnum
import com.hotaku.ui.conposables.DynamicTopAppBarColumn
import com.hotaku.ui.conposables.MediaGrid
import com.hotaku.ui.conposables.TopAppBar

@Composable
fun MediaLibraryScreen(modifier: Modifier = Modifier) {
    val mediaLibraryViewModel = hiltViewModel<MediaLibraryViewModel>()

    val screenState by mediaLibraryViewModel.mediaLibraryScreenUiState.collectAsStateWithLifecycle()

    MediaLibraryScreenContent(
        modifier = modifier,
        state = screenState,
        onAction = { mediaLibraryViewModel.onAction(it) },
    )
}

@Composable
private fun MediaLibraryScreenContent(
    modifier: Modifier = Modifier,
    state: MediaLibraryUiState,
    onAction: (MediaLibraryScreenActions) -> Unit,
) {
    val mediaPagingItems = state.media?.collectAsLazyPagingItems()

    val refreshState = mediaPagingItems?.loadState?.refresh

    val windowSize = currentWindowAdaptiveInfo().windowSizeClass

    val isCompact = windowSize.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    BackHandler(state.selectedFolder != null) {
        onAction(OnClearMedia)
    }

    LaunchedEffect(state.selectedFolder) {
        if (state.selectedFolder == null) {
            onAction(OnClearMedia)
        } else {
            onAction(OnUpdateMediaState)
        }
    }

    DynamicTopAppBarColumn(
        modifier = modifier.fillMaxSize(),
        animatableTopContent = {
            TopAppBar(title = stringResource(id = R.string.media_library_top_bar_title))
        },
        content = {
            AnimatedContent(
                targetState = state.selectedFolder != null,
            ) { selectedFolder ->
                if (selectedFolder && mediaPagingItems != null) {
                    MediaGrid(
                        pagingMediaItems = mediaPagingItems,
                        onScrolled = {},
                        onItemClick = {},
                        onItemLongClick = {},
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        if (isCompact) {
                            VerticalLibraryFolders { item ->
                                LibraryFolderItem(
                                    isCompact = isCompact,
                                    item = item,
                                    onItemClick = {
                                        onFolderItemClick(item = item.item, onAction = onAction)
                                    },
                                )
                            }
                        } else {
                            HorizontalLibraryFolders { item ->
                                LibraryFolderItem(
                                    modifier = Modifier.padding(8.dp),
                                    isCompact = isCompact,
                                    item = item,
                                    onItemClick = {
                                        onFolderItemClick(item = item.item, onAction = onAction)
                                    },
                                )
                            }
                        }
                    }
                }
            }
        },
    )
}

private fun onFolderItemClick(
    item: LibraryItemsEnum,
    onAction: (MediaLibraryScreenActions) -> Unit,
) {
    when (item) {
        LibraryItemsEnum.FAVORITE_FOLDER -> onAction(OnFavoriteFolderClick)
        LibraryItemsEnum.TRASH_FOLDER -> onAction(OnTrashFolderClick)
        LibraryItemsEnum.SECURE_FOLDER -> onAction(OnSecureFolderClick)
    }
}

@PreviewScreenSizes
@Composable
private fun MediaLibraryScreenContentPreview() {
    MiniGalleryTheme {
        MediaLibraryScreenContent(
            state = MediaLibraryUiState(),
            onAction = {},
        )
    }
}
