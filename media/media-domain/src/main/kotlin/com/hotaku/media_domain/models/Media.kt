package com.hotaku.media_domain.models

data class Media(
    val mediaId: Long,
    val uriString: String,
    val displayName: String,
    val mimeType: String,
    val duration: String,
    val dateAdded: Long,
    val dateModified: Long,
    val size: Long,
    val isTrash: Boolean,
    val isFavorite: Boolean,
    val bucketDisplayName: String,
)
