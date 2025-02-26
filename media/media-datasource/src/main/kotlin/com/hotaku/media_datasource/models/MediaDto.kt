package com.hotaku.media_datasource.models

internal data class MediaDto(
    val mediaId: Long,
    val uriString: String,
    val displayName: String,
    val mimeType: String,
    val duration: String,
    val dateAdded: Long,
    val dateModified: Long,
    val size: Long,
    val bucketDisplayName: String,
)
