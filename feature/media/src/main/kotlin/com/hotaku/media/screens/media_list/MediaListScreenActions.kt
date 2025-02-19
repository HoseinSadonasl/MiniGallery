package com.hotaku.media.screens.media_list

import com.hotaku.media.model.AlbumUi

sealed interface MediaListScreenActions {
    data object OnUpdateMediaList : MediaListScreenActions

    data class OnQueryChange(val query: String) : MediaListScreenActions

    data class OnMimeTypeChange(val mimeType: String) : MediaListScreenActions

    data object OnHideSyncSection : MediaListScreenActions

    data class OnSetTopBarVisibility(val visible: Boolean) : MediaListScreenActions

    data object OnExpandSearch : MediaListScreenActions

    data object OnCollepseSearch : MediaListScreenActions

    data class OnAlbumSelected(val album: AlbumUi?) : MediaListScreenActions

    data class OnMediaListClick(val mediaItemIndex: Int) : MediaListScreenActions

    data object OnMediaListLongClick : MediaListScreenActions

    data object OnClearSelectedMedia : MediaListScreenActions

    data object OnOpenMedia : MediaListScreenActions

    data object OnShareMedia : MediaListScreenActions

    data object OnShowDeleteMediaDialog : MediaListScreenActions

    data object OnCloseDialog : MediaListScreenActions

    data class OnDeleteMedia(val mediaUri: String) : MediaListScreenActions
}
