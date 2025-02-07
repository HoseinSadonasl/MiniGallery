package com.hotaku.media.screens.media

data class MediaUiState(
    val isSearchExpanded: Boolean = false,
    val showSyncSection: Boolean = true,
    val isScrolled: Boolean = true,
    val mimeType: String = "",
    val query: String = "",
)
