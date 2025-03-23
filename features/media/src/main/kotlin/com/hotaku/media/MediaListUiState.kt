package com.hotaku.media

import androidx.paging.PagingData
import com.hotaku.ui.MediaDialogs
import com.hotaku.ui.UiState
import com.hotaku.ui.models.MediaUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal data class MediaListUiState(
    val synchronize: UiState<Int>? = null,
    val media: Flow<PagingData<MediaUi>> = emptyFlow(),
    val isSearchExpanded: Boolean = false,
    val showSyncSection: Boolean = true,
    val isScrolling: Boolean = false,
    val isTopBarVisible: Boolean = true,
    val isOptionsVisible: Boolean = false,
    val isOptionsMenuVisible: Boolean = false,
    val dialog: MediaDialogs = MediaDialogs.Idle,
    val selectedMediaIndex: Int? = null,
    val isSearchFocused: Boolean = false,
    val selectedItemName: String = "",
    val mimeType: String = "",
    val query: String = "",
    val albumName: String = "",
    val mediaNameQuery: String = "",
)
