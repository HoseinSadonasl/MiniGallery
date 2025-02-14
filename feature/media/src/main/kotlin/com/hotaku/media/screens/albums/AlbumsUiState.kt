package com.hotaku.media.screens.albums

import androidx.paging.compose.LazyPagingItems
import com.hotaku.media.model.AlbumUi
import com.hotaku.media.model.MediaUi
import com.hotaku.ui.UiState

internal data class AlbumsUiState(
    val albums: UiState<List<AlbumUi>> = UiState.Success(emptyList()),
    val selectedAlbum: AlbumUi? = null,
    val mediaList: LazyPagingItems<MediaUi>? = null,
)
