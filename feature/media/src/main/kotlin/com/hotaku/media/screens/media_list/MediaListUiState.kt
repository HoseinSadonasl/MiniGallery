package com.hotaku.media.screens.media_list

import com.hotaku.media.model.AlbumUi

internal data class MediaListUiState(
    val isSearchExpanded: Boolean = false,
    val showSyncSection: Boolean = true,
    val isTopBarVisible: Boolean = true,
    val selectedAlbum: AlbumUi? = null,
    val selectedMediaIndex: Int? = null,
    val mimeType: String = "",
    val query: String = "",
    val mediaListDialogs: MediaListScreenDialogs? = null,
)
