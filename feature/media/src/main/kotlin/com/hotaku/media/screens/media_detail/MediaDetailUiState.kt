package com.hotaku.media.screens.media_detail

import com.hotaku.media.model.MediaUi

internal data class MediaDetailUiState(
    val media: List<MediaUi> = emptyList(),
    val selectedMediaItemIndex: Int = 0,
    val mediaName: String = "",
    val openMenuPopup: Boolean = false,
    val openRenameDialog: Boolean = false,
)
