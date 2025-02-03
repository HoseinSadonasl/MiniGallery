package com.hotaku.home

sealed interface HomeScreenActions {
    data object OnUpdateMedia : HomeScreenActions

    data class OnQueryChange(val query: String) : HomeScreenActions

    data class OnMimeTypeChange(val mimeType: String) : HomeScreenActions

    data object OnHideSyncSection : HomeScreenActions

    data class OnScrolled(val isScrolled: Boolean) : HomeScreenActions

    data object OnExpandSearch : HomeScreenActions

    data object OnCollepseSearch : HomeScreenActions
}
