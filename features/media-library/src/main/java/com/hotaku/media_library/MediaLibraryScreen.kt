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
import com.hotaku.media_library.MediaLibraryScreenActions.OnClearMedia
import com.hotaku.media_library.MediaLibraryScreenActions.OnFolderClick
import com.hotaku.media_library.MediaLibraryScreenActions.OnUpdateMediaState
import com.hotaku.media_library.composables.HorizontalLibraryFolders
import com.hotaku.media_library.composables.LibraryFolderItem
import com.hotaku.media_library.composables.VerticalLibraryFolders
import com.hotaku.media_library.utils.LibraryFolderType
import com.hotaku.ui.asString
import com.hotaku.ui.conposables.DynamicTopAppBarColumn
import com.hotaku.ui.conposables.MediaGrid
import com.hotaku.ui.conposables.TopAppBar

@Composable
fun MediaLibraryScreen(
    modifier: Modifier = Modifier,
    navigateToMediaDetailScreen: (Int, LibraryFolderType) -> Unit,
) {
    val mediaLibraryViewModel = hiltViewModel<MediaLibraryViewModel>()

    val screenState by mediaLibraryViewModel.mediaLibraryScreenUiState.collectAsStateWithLifecycle()

    MediaLibraryScreenContent(
        modifier = modifier,
        state = screenState,
        onAction = { mediaLibraryViewModel.onAction(it) },
        navigateToMediaDetailScreen = navigateToMediaDetailScreen,
    )
}

@Composable
private fun MediaLibraryScreenContent(
    modifier: Modifier = Modifier,
    state: MediaLibraryUiState,
    onAction: (MediaLibraryScreenActions) -> Unit,
    navigateToMediaDetailScreen: (Int, LibraryFolderType) -> Unit,
) {
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass
    val isCompact = windowSize.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    val mediaPagingItems = state.media?.collectAsLazyPagingItems()

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
            TopAppBar(title = state.screenTitle?.asString() ?: stringResource(id = R.string.media_library_top_bar_title))
        },
        content = {
            AnimatedContent(
                targetState = state.selectedFolder != null,
            ) { selectedFolder ->
                if (selectedFolder && mediaPagingItems != null) {
                    MediaGrid(
                        pagingMediaItems = mediaPagingItems,
                        onScrolled = {},
                        onItemClick = { index ->
                            navigateToMediaDetailScreen(index, state.selectedFolder!!)
                        },
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
                                        onAction(OnFolderClick(folderItem = item))
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
                                        onAction(OnFolderClick(folderItem = item))
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

@PreviewScreenSizes
@Composable
private fun MediaLibraryScreenContentPreview() {
    MiniGalleryTheme {
        MediaLibraryScreenContent(
            state = MediaLibraryUiState(),
            onAction = {},
            navigateToMediaDetailScreen = { _, _ -> },
        )
    }
}
