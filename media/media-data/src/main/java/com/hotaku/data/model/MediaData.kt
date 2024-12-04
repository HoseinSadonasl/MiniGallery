package com.hotaku.data.model

data class MediaData(
    val mediaId: Long,
    val path: String,
    val displayName: String,
    val mimeType: String,
    val dateAdded: Long,
    val dateModified: Long,
    val size: Long,
)
