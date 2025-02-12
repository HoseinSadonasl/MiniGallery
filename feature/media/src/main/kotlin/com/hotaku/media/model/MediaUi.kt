package com.hotaku.media.model

import android.os.Parcelable
import com.hotaku.media.utils.MediaType
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
data class MediaUi(
    val mediaId: Long,
    val uriString: String,
    val displayName: String,
    val mimeType: MediaType,
    val duration: String,
    val dateAdded: Instant,
    val dateModified: Instant,
    val size: Long,
    val bucketDisplayName: String,
) : Parcelable
