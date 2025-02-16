package com.hotaku.media.screens.media_detail

import com.hotaku.media.model.MediaUi

internal sealed interface MediaDetailScreenActions {
    data class OnAddmediaList(val media: List<MediaUi>) : MediaDetailScreenActions

    data object OnOOpenMenu : MediaDetailScreenActions

    data object OnCloseMenu : MediaDetailScreenActions

    data object OnRenameClick : MediaDetailScreenActions

    data object OnSubmitRenameClick : MediaDetailScreenActions

    data class OnNameChange(val newName: String) : MediaDetailScreenActions

    data object OnDeleteMedia : MediaDetailScreenActions

    data object OnUpdateMedia : MediaDetailScreenActions
}
