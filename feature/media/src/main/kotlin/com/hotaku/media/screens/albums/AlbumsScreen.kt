package com.hotaku.media.screens.albums

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.feature.media.R
import com.hotaku.media.components.ImageThumbnail
import com.hotaku.media.components.ScreenMessage
import com.hotaku.media.components.VideoThumbnail
import com.hotaku.media.model.AlbumUi
import com.hotaku.media.utils.MediaType
import com.hotaku.ui.UiState
import com.hotaku.ui.conposables.DynamicTopAppBarColumn
import com.hotaku.ui.conposables.TopAppBar
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun AlbumsScreen(
    modifier: Modifier = Modifier,
    albumsViewModel: AlbumsViewModel,
    onNavigateWithAlbum: (AlbumUi) -> Unit,
) {
    val state by albumsViewModel.albumsState.collectAsState()

    LaunchedEffect(state.albums) {
        println(state.albums.toString())
    }

    LaunchedEffect(albumsViewModel.event) {
        albumsViewModel.event.collectLatest { event ->
            when (event) {
                is AlbumsScreenEvents.OnNavigateToWithAlbum -> {
                    onNavigateWithAlbum(event.albumUi)
                }
            }
        }
    }

    AlbumsScreen(
        modifier = modifier,
        albumsState = state.albums,
        onAction = albumsViewModel::onAction,
    )
}

@Composable
private fun AlbumsScreen(
    modifier: Modifier = Modifier,
    albumsState: UiState<List<AlbumUi>>,
    onAction: (AlbumsScreenActions) -> Unit,
) {
    DynamicTopAppBarColumn(
        modifier = modifier,
        animatableTopContent = {
            TopAppBar(
                title = stringResource(R.string.albums_screen_top_app_bar_title),
            )
        },
    ) {
        when (albumsState) {
            is UiState.Failure -> {
            }
            is UiState.Loading -> {
            }
            is UiState.Success -> {
                AlbumsGridList(
                    albums = albumsState.data,
                )
            }
        }
    }
}

@Composable
private fun NoAlbums() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        ScreenMessage(
            title = stringResource(id = R.string.albums_screen_no_albums),
            fulMessage = stringResource(id = R.string.albums_screen_no_albums_full_message),
        )
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
fun AlbumsGridList(
    modifier: Modifier = Modifier,
    albums: List<AlbumUi>,
) {
    if (albums.isEmpty()) {
        NoAlbums()
    } else {
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Adaptive(100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
        ) {
            items(
                items = albums,
                key = { it.thumbnailUriString },
            ) { album ->
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = album.displayName,
                            maxLines = 1,
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Text(
                            text = album.count.toString(),
                            maxLines = 1,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    when (album.thumbnailType) {
                        MediaType.IMAGE -> {
                            ImageThumbnail(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .clip(MaterialTheme.shapes.large),
                                itemUri = album.thumbnailUriString,
                            )
                        }
                        MediaType.VIDEO -> {
                            VideoThumbnail(
                                itemUri = album.thumbnailUriString,
                            )
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}
