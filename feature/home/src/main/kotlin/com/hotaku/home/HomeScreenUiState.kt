package com.hotaku.home

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val shoSyncSection: Boolean = true,
    val isScrolled: Boolean = true,
    val mimeType: String = "",
    val query: String = "",
)
