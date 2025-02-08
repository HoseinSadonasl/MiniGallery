package com.hotaku.media.screens.albums

import com.hotaku.media.model.AlbumUi

internal data class AlbumsUiState(
    val isLoading: Boolean = false,
    val albums: List<AlbumUi> = emptyList(),
    val selectedAlbum: AlbumUi? = null,
)
