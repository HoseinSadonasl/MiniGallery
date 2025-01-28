package com.hotaku.home

sealed interface HomeScreenActions {
    data object OnUpdateMedia : HomeScreenActions

    data object OnNoMedia : HomeScreenActions

    data class OnQueryChange(val query: String) : HomeScreenActions

    data class OnMimeTypeChange(val mimeType: String) : HomeScreenActions

    data object OnHideSyncSection : HomeScreenActions
}
