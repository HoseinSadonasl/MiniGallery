package com.hotaku.media_details

import androidx.paging.PagingData
import com.hotaku.ui.MediaDialogs
import com.hotaku.ui.models.MediaUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal data class MediaDetailUiState(
    val selectedAlbumName: String = "",
    val mediaName: String = "",
    val media: Flow<PagingData<MediaUi>> = emptyFlow(),
    val selectedMediaIndex: Int = 0,
    val mediaNameQuery: String = "",
    val isOptionsVisible: Boolean = false,
    val isOptionsMenuVisible: Boolean = false,
    val dialog: MediaDialogs = MediaDialogs.Idle,
)
