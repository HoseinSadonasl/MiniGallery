package com.hotaku.media.screens.media

import com.hotaku.media.model.AlbumUi

sealed interface MediaScreenActions {
    data object OnUpdateMedia : MediaScreenActions

    data class OnQueryChange(val query: String) : MediaScreenActions

    data class OnMimeTypeChange(val mimeType: String) : MediaScreenActions

    data object OnHideSyncSection : MediaScreenActions

    data class OnSetTopBarVisibility(val visible: Boolean) : MediaScreenActions

    data object OnExpandSearch : MediaScreenActions

    data object OnCollepseSearch : MediaScreenActions

    data class OnAlbumSelected(val album: AlbumUi?) : MediaScreenActions

    data class OnMediaClick(val mediaItemIndex: Int) : MediaScreenActions

    data object OnMediaLongClick : MediaScreenActions

    data object OnClearSelectedMedia : MediaScreenActions

    data object OnOpenMedia : MediaScreenActions

    data object OnShareMedia : MediaScreenActions

    data object OnDeleteMedia : MediaScreenActions
}
