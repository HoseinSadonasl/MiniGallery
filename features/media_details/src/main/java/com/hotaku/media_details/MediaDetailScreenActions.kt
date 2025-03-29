package com.hotaku.media_details

import com.hotaku.ui.models.MediaUi

internal sealed interface MediaDetailScreenActions {
    data class OnSelectedIndexChanged(val index: Int) : MediaDetailScreenActions

    data class OnTrashMedia(val mediaItem: MediaUi) : MediaDetailScreenActions

    data class OnItemIsFavoriteChange(val mediaItem: MediaUi) : MediaDetailScreenActions

    data class OnMediaNameChange(val mediaName: String) : MediaDetailScreenActions

    data object OnShowOptions : MediaDetailScreenActions

    data object OnHideOptions : MediaDetailScreenActions

    data object OnShowOptionsMenu : MediaDetailScreenActions

    data object OnHideOptionsMenu : MediaDetailScreenActions

    data object OnHideDialog : MediaDetailScreenActions

    data object OnShowRenameMediaDialog : MediaDetailScreenActions

    data object OnShowDetails : MediaDetailScreenActions

    data class OnMediaNameQueryChange(val query: String) : MediaDetailScreenActions

    data object OnClearMediaNameQuery : MediaDetailScreenActions

    data class OnRenameMediaItem(val media: MediaUi) : MediaDetailScreenActions
}
