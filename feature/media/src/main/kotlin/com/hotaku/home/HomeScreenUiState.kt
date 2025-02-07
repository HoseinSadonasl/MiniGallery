package com.hotaku.home

data class HomeScreenUiState(
    val isSearchExpanded: Boolean = false,
    val showSyncSection: Boolean = true,
    val isScrolled: Boolean = true,
    val mimeType: String = "",
    val query: String = "",
)
