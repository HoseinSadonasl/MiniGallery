package com.hotaku.media.screens.media_detail

import androidx.paging.PagingData
import com.hotaku.media.model.MediaUi

internal data class MediaDetailUiState(
    val media: PagingData<MediaUi> = PagingData.empty(),
    val selectedMediaItemIndex: Int = 0,
    val mediaName: String = "",
    val openMenuPopup: Boolean = false,
    val openRenameDialog: Boolean = false,
)
