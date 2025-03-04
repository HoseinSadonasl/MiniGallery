package com.hotaku.media.screens.media_list

internal data class MediaListUiState(
    val isSearchExpanded: Boolean = false,
    val showSyncSection: Boolean = true,
    val isTopBarVisible: Boolean = true,
    val selectedMediaIndex: Int? = null,
    val mimeType: String = "",
    val query: String = "",
    val albumName: String = "",
)
