package com.hotaku.home.model

import java.time.Instant

data class MediaUi(
    val mediaId: Long,
    val uriString: String,
    val displayName: String,
    val mimeType: String,
    val duration: String,
    val dateAdded: Instant,
    val dateModified: Instant,
    val size: Long,
)
