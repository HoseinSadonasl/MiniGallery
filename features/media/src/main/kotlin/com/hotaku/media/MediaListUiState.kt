package com.hotaku.media

import com.hotaku.ui.MediaDialogs

internal data class MediaListUiState(
    val isSearchExpanded: Boolean = false,
    val showSyncSection: Boolean = true,
    val isTopBarVisible: Boolean = true,
    val isMenuVisible: Boolean = false,
    val isOptionsVisible: Boolean = false,
    val mediaDialog: MediaDialogs = MediaDialogs.Idle,
    val selectedMediaIndex: Int? = null,
    val mimeType: String = "",
    val query: String = "",
    val albumName: String = "",
    val mediaNameQuery: String = "",
)
