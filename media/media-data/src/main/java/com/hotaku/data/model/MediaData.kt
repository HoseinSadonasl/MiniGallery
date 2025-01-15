package com.hotaku.data.model

data class MediaData(
    val mediaId: Long,
    val uriString: String,
    val displayName: String,
    val mimeType: String,
    val duration: String,
    val dateAdded: Long,
    val dateModified: Long,
    val size: Long,
)
