package com.hotaku.media.model

import com.hotaku.media.utils.MediaType

data class AlbumUi(
    val albumName: String,
    val cover: Pair<String, MediaType>,
    val itemsCount: Int,
)
