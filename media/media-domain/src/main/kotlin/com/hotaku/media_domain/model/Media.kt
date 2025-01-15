package com.hotaku.media_domain.model

data class Media(
    val mediaId: Long,
    val uriString: String,
    val displayName: String,
    val mimeType: String,
    val duration: String,
    val dateAdded: Long,
    val dateModified: Long,
    val size: Long,
)
