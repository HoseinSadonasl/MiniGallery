package com.hotaku.media.model

import com.hotaku.media.utils.MediaType

data class AlbumUi(
    val displayName: String,
    val thumbnailUriString: String,
    val thumbnailType: MediaType,
    val count: Int,
)
