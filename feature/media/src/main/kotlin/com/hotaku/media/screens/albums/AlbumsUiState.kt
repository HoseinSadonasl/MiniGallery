package com.hotaku.media.screens.albums

import com.hotaku.media.model.AlbumUi
import com.hotaku.ui.UiState

internal data class AlbumsUiState(
    val albums: UiState<List<AlbumUi>> = UiState.Success(emptyList()),
    val selectedAlbum: AlbumUi? = null,
)
