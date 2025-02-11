package com.hotaku.media.screens.media

import com.hotaku.media.model.AlbumUi
import com.hotaku.media.model.MediaUi

data class MediaUiState(
    val isSearchExpanded: Boolean = false,
    val showSyncSection: Boolean = true,
    val isScrolled: Boolean = true,
    val selectedAlbum: AlbumUi? = null,
    val selectedMedia: MediaUi? = null,
    val mimeType: String = "",
    val query: String = "",
)
