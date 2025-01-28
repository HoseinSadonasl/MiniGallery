package com.hotaku.home

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val shoSyncSection: Boolean = true,
    val noMedia: Boolean = false,
    val mimeType: String = "",
    val query: String = "",
)
