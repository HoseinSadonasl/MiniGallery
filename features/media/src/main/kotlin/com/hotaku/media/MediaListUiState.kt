package com.hotaku.media

import com.hotaku.ui.MediaDialogs

internal data class MediaListUiState(
    val isSearchExpanded: Boolean = false,
    val showSyncSection: Boolean = true,
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
