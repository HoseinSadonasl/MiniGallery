package com.hotaku.media_details

import com.hotaku.ui.models.MediaUi

internal sealed interface MediaDetailScreenActions {
    data class OnSelectedIndexChanged(val index: Int) : MediaDetailScreenActions

    data object OnPlayVideo : MediaDetailScreenActions

    data object OnShareMedia : MediaDetailScreenActions

    data object OnOOpenMenu : MediaDetailScreenActions

    data object OnCloseMenu : MediaDetailScreenActions

    data object OnRenameClick : MediaDetailScreenActions

    data object OnSubmitRenameClick : MediaDetailScreenActions

    data class OnNameChange(val newName: String) : MediaDetailScreenActions

    data class OnDeleteMedia(val mediaItem: MediaUi) : MediaDetailScreenActions

    data object OnShowOptions : MediaDetailScreenActions

    data object OnHideOptions : MediaDetailScreenActions

    data object OnUpdateMedia : MediaDetailScreenActions
}
