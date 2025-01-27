package com.hotaku.home

sealed interface HomeScreenActions {
    data object UpdateMedia : HomeScreenActions

    data class OnQueryChange(val query: String) : HomeScreenActions

    data class OnMimeTypeChange(val mimeType: String) : HomeScreenActions
}
