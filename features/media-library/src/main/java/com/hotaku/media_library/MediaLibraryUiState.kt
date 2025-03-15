package com.hotaku.media_library

import com.hotaku.media_library.utils.LibraryItemsEnum

internal data class MediaLibraryUiState(
    val selectedFolder: LibraryItemsEnum? = null,
)
