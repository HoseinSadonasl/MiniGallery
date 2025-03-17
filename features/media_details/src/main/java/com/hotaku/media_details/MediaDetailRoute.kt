package com.hotaku.media_details

import kotlinx.serialization.Serializable

@Serializable
data class MediaDetailRoute(
    val initialItemIndex: Int?,
    val selectedAlbum: String? = null,
    val matchTrash: Boolean = false,
    val matchFavorite: Boolean = false,
)
