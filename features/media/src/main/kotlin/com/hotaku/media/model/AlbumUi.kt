package com.hotaku.media.model

import com.hotaku.ui.MediaType

internal data class AlbumUi(
    val displayName: String,
    val thumbnailUriString: String,
    val thumbnailType: MediaType,
    val count: Int,
)
