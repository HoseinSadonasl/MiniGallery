package com.hotaku.home

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val mimeType: String = "",
    val query: String = "",
)
