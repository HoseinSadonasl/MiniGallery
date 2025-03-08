package com.hotaku.media_details

import androidx.paging.PagingData
import com.hotaku.ui.models.MediaUi

internal data class MediaDetailUiState(
    val selectedAlbumName: String = "",
    val media: PagingData<MediaUi> = PagingData.empty(),
    val selectedMediaItemIndex: Int = 0,
    val mediaName: String = "",
    val openMenuPopup: Boolean = false,
    val openRenameDialog: Boolean = false,
    val isOptionsVisible: Boolean = false,
)
