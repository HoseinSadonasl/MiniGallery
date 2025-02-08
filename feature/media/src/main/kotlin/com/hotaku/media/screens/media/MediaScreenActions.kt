package com.hotaku.media.screens.media

import com.hotaku.media.model.AlbumUi

sealed interface MediaScreenActions {
    data object OnUpdateMedia : MediaScreenActions

    data class OnQueryChange(val query: String) : MediaScreenActions

    data class OnMimeTypeChange(val mimeType: String) : MediaScreenActions

    data object OnHideSyncSection : MediaScreenActions

    data class OnScrolled(val isScrolled: Boolean) : MediaScreenActions

    data object OnExpandSearch : MediaScreenActions

    data object OnCollepseSearch : MediaScreenActions

    data class OnAlbumSelected(val album: AlbumUi) : MediaScreenActions

    data object OnClearSelectedAlbum : MediaScreenActions
}
