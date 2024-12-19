package com.hotaku.media_domain.model

data class Media(
    val mediaId: Long,
    val path: String,
    val displayName: String,
    val mimeType: String,
    val dateAdded: Long,
    val dateModified: Long,
    val size: Long,
)
