package com.hotaku.ui.models

import com.hotaku.ui.MediaType
import java.time.Instant

data class MediaUi(
    val mediaId: Long,
    val uriString: String,
    val displayName: String,
    val mimeType: MediaType,
    val duration: Int,
    val dateAdded: Instant,
    val dateModified: Instant,
    val size: Long,
    val isTrash: Boolean,
    val isFavorite: Boolean,
    val bucketDisplayName: String,
)
