package com.hotaku.media.screens.media

import com.hotaku.media.model.AlbumUi

data class MediaUiState(
    val isSearchExpanded: Boolean = false,
    val showSyncSection: Boolean = true,
    val isScrolled: Boolean = true,
    val selectedAlbum: AlbumUi? = null,
    val mimeType: String = "",
    val query: String = "",
)
