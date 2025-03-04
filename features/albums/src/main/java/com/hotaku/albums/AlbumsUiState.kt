package com.hotaku.albums

import com.hotaku.albums.model.AlbumUi
import com.hotaku.ui.UiState

internal data class AlbumsUiState(
    val albums: UiState<List<AlbumUi>> = UiState.Success(emptyList()),
    val selectedAlbum: AlbumUi? = null,
    val selectedMediaIndex: Int? = null,
    val mimeType: String = "",
    val query: String = "",
)
