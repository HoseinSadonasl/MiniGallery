package com.hotaku.media_library

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.features.media_library.R
import com.hotaku.media_library.composables.HorizontalLibraryFolders
import com.hotaku.media_library.composables.LibraryFolderItem
import com.hotaku.media_library.composables.VerticalLibraryFolders
import com.hotaku.ui.conposables.DynamicTopAppBarColumn
import com.hotaku.ui.conposables.TopAppBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MediaLibraryScreen(modifier: Modifier = Modifier) {
    val mediaLibraryViewModel = hiltViewModel<MediaLibraryViewModel>()

    MediaLibraryScreenContent(
        modifier = modifier,
        screenState = mediaLibraryViewModel.mediaLibraryScreenUiState,
        onAction = { mediaLibraryViewModel.onAction(it) },
    )
}

@Composable
private fun MediaLibraryScreenContent(
    modifier: Modifier = Modifier,
    screenState: StateFlow<MediaLibraryUiState>,
    onAction: (MediaLibraryScreenActions) -> Unit,
) {
    val state by screenState.collectAsStateWithLifecycle()

    val windowSize = currentWindowAdaptiveInfo().windowSizeClass

    val isCompact = windowSize.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    DynamicTopAppBarColumn(
        modifier = modifier.fillMaxSize(),
        animatableTopContent = {
            TopAppBar(title = stringResource(id = R.string.media_library_top_bar_title))
        },
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (isCompact) {
                    VerticalLibraryFolders { item ->
                        LibraryFolderItem(
                            isCompact = isCompact,
                            item = item,
                            onItemClick = {},
                        )
                    }
                } else {
                    HorizontalLibraryFolders { item ->
                        LibraryFolderItem(
                            modifier = Modifier.padding(8.dp),
                            isCompact = isCompact,
                            item = item,
                            onItemClick = {},
                        )
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
            screenState = MutableStateFlow(MediaLibraryUiState()),
            onAction = {},
        )
    }
}
