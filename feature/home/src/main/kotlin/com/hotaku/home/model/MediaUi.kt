package com.hotaku.home.model

import com.hotaku.home.utils.MediaType
import java.time.Instant

data class MediaUi(
    val mediaId: Long,
    val uriString: String,
    val displayName: String,
    val mimeType: MediaType,
    val duration: String,
    val dateAdded: Instant,
    val dateModified: Instant,
    val size: Long,
)
