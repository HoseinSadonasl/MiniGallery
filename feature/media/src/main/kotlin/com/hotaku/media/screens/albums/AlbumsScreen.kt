package com.hotaku.media.screens.albums

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.feature.media.R
import com.hotaku.media.components.ImageThumbnail
import com.hotaku.media.components.MediaGrid
import com.hotaku.media.components.OnScreenMessage
import com.hotaku.media.components.VideoThumbnail
import com.hotaku.media.model.AlbumUi
import com.hotaku.media.model.MediaUi
import com.hotaku.media.utils.MediaType
import com.hotaku.ui.UiState
import com.hotaku.ui.conposables.DynamicTopAppBarColumn
import com.hotaku.ui.conposables.ShimmerPlaceHolder
import com.hotaku.ui.conposables.TopAppBar
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun AlbumsScreen(
    modifier: Modifier = Modifier,
    albumsViewModel: AlbumsViewModel,
) {
    AlbumsScreen(
        modifier = modifier,
        albumsViewModel = albumsViewModel,
        onAction = albumsViewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun AlbumsScreen(
    modifier: Modifier = Modifier,
    albumsViewModel: AlbumsViewModel,
    onAction: (AlbumsScreenActions) -> Unit,
) {
    val state by albumsViewModel.albumsState.collectAsStateWithLifecycle()

    val navigator = rememberSupportingPaneScaffoldNavigator<LazyPagingItems<MediaUi>>()

    BackHandler(navigator.canNavigateBack()) {
        onAction(AlbumsScreenActions.OnCloseAlbum)
        navigator.navigateBack()
    }

    LaunchedEffect(albumsViewModel.event) {
        albumsViewModel.event.collectLatest { event ->
            when (event) {
                is AlbumsScreenEvents.OnNavigateToMediaPane -> {
                    navigator.navigateTo(ThreePaneScaffoldRole.Secondary, state.mediaList)
                }
            }
        }
    }

    LaunchedEffect(state.selectedAlbum) {
        if (state.selectedAlbum != null && state.mediaList != null) {
            navigator.navigateTo(ThreePaneScaffoldRole.Secondary, state.mediaList)
        }
    }

    DynamicTopAppBarColumn(
        modifier = modifier,
        animatableTopContent = {
            TopAppBar(
                title = stringResource(R.string.albums_screen_top_app_bar_title),
            )
        },
        content = {
            SupportingPaneScaffold(
                directive = navigator.scaffoldDirective,
                value = navigator.scaffoldValue,
                mainPane = {
                    AnimatedPane {
                        AlbumsGridList(
                            albumsListState = state.albums,
                            onAction = onAction,
                        )
                    }
                },
                supportingPane = {
                    AnimatedPane {
                        navigator.currentDestination?.content?.let { mediaPagingItems ->
                            MediaGrid(
                                pagingMediaItems = mediaPagingItems,
                                onScrolled = { scrolled ->
                                },
                                onItemClick = { media ->
                                },
                                onItemLongClick = {},
                            )
                        }
                    }
                },
            )
        },
    )
}

@Composable
private fun NoAlbums() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        OnScreenMessage(
            title = stringResource(id = R.string.albums_screen_no_albums),
            fulMessage = stringResource(id = R.string.albums_screen_no_albums_full_message),
        )
    }
}

@Composable
private fun AlbumsLoadError() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        OnScreenMessage(
            color = MaterialTheme.colorScheme.error,
            title = stringResource(id = R.string.albums_screen_error_while_getting_albums),
            fulMessage = stringResource(id = R.string.albums_screen_error_while_getting_albums_full_message),
        )
    }
}

@Preview(showBackground = true)
@PreviewScreenSizes
@Composable
private fun ErrorGettingAlbumsPreview() {
    MiniGalleryTheme {
        AlbumsLoadError()
    }
}

@Preview(showBackground = true)
@PreviewScreenSizes
@Composable
private fun NoAlbumsPreview() {
    MiniGalleryTheme {
        NoAlbums()
    }
}

@Composable
private fun AlbumsGridList(
    modifier: Modifier = Modifier,
    albumsListState: UiState<List<AlbumUi>>,
    onAction: (AlbumsScreenActions) -> Unit,
) {
    when {
        albumsListState is UiState.Failure -> {
            AlbumsLoadError()
        }
        albumsListState is UiState.Success && albumsListState.data.isEmpty() -> {
            NoAlbums()
        }
        else -> {
            LazyVerticalGrid(
                modifier = modifier.fillMaxSize(),
                columns = GridCells.Adaptive(120.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp),
            ) {
                when (albumsListState) {
                    is UiState.Loading -> {
                        loadingItems()
                    }
                    is UiState.Success -> {
                        albumsListItems(
                            albumsListState = albumsListState,
                            onAlbumClick = onAction,
                        )
                    }
                    else -> {
                        // Do nothing
                    }
                }
            }
        }
    }
}

private fun LazyGridScope.albumsListItems(
    albumsListState: UiState.Success<List<AlbumUi>>,
    onAlbumClick: (AlbumsScreenActions) -> Unit,
) {
    items(
        items = albumsListState.data,
        key = { it.thumbnailUriString },
    ) { album ->
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { onAlbumClick(AlbumsScreenActions.OnAlbumClick(album)) },
        ) {
            when (album.thumbnailType) {
                MediaType.IMAGE -> {
                    ImageThumbnail(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .clip(MaterialTheme.shapes.medium),
                        itemUri = album.thumbnailUriString,
                    )
                }

                MediaType.VIDEO -> {
                    VideoThumbnail(
                        Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.medium),
                        itemUri = album.thumbnailUriString,
                    )
                }

                else -> {
                    ShimmerPlaceHolder(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .clip(MaterialTheme.shapes.medium),
                    )
                }
            }
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(MaterialTheme.colorScheme.background.copy(alpha = .7f))
                        .padding(4.dp),
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = album.displayName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = album.count.toString(),
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

private fun LazyGridScope.loadingItems() {
    items(6) {
        ShimmerPlaceHolder(
            modifier = Modifier.clip(MaterialTheme.shapes.medium),
        )
    }
}
