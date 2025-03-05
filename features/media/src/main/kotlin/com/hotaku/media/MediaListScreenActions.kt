package com.hotaku.media

import com.hotaku.ui.models.MediaUi

internal sealed interface MediaListScreenActions {
    data object OnUpdateUpdateMedia : MediaListScreenActions

    data class OnQueryChange(val query: String) : MediaListScreenActions

    data class OnMimeTypeChange(val mimeType: String) : MediaListScreenActions

    data object OnRetrySynchronizeMedia : MediaListScreenActions

    data object OnHideSyncSection : MediaListScreenActions

    data class OnSetTopBarVisibility(val visible: Boolean) : MediaListScreenActions

    data object OnExpandSearch : MediaListScreenActions

    data object OnCollepseSearch : MediaListScreenActions

    data class OnMediaListItemClick(val mediaItemIndex: Int) : MediaListScreenActions

    data object OnMediaListItemLongClick : MediaListScreenActions

    data object OnClearSelectedMedia : MediaListScreenActions

    data object OnOpenMediaDetails : MediaListScreenActions

    data object OnPlayVideo : MediaListScreenActions

    data object OnShareMedia : MediaListScreenActions

    data class OnDeleteMediaItem(val mediaItem: MediaUi) : MediaListScreenActions
}
