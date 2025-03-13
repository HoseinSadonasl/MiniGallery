package com.hotaku.media

import com.hotaku.ui.models.MediaUi

internal sealed interface MediaListScreenActions {
    data object OnUpdateUpdateMedia : MediaListScreenActions

    data class OnSearchQueryChange(val query: String) : MediaListScreenActions

    data class OnMimeTypeChange(val mimeType: String) : MediaListScreenActions

    data class OnSearchFocusChanged(val hasFocus: Boolean) : MediaListScreenActions

    data object OnRetrySynchronizeMedia : MediaListScreenActions

    data object OnHideSyncSection : MediaListScreenActions

    data class OnSetTopBarVisibility(val visible: Boolean) : MediaListScreenActions

    data class OnMediaListItemClick(val mediaItemIndex: Int) : MediaListScreenActions

    data class OnSelectedMediaNameChange(val mediaName: String) : MediaListScreenActions

    data object OnMediaListItemLongClick : MediaListScreenActions

    data object OnClearSelectedMedia : MediaListScreenActions

    data object OnOpenMediaDetails : MediaListScreenActions

    data object OnPlayVideo : MediaListScreenActions

    data object OnShareMedia : MediaListScreenActions

    data class OnTrashMediaItem(val mediaItem: MediaUi) : MediaListScreenActions

    data object OnShowOptions : MediaListScreenActions

    data object OnHideOptions : MediaListScreenActions

    data object OnShowOptionsMenu : MediaListScreenActions

    data object OnHideOptionsMenu : MediaListScreenActions

    data object OnHideDiaDialog : MediaListScreenActions

    data object OnOpenRenameMediaDialog : MediaListScreenActions

    data class OnMediaNameQueryChange(val query: String) : MediaListScreenActions

    data object OnMediaNameClearQuery : MediaListScreenActions

    data class OnRenameMediaItem(val media: MediaUi) : MediaListScreenActions

    data object ShowDetails : MediaListScreenActions
}
