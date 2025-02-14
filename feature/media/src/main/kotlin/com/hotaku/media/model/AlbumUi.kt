package com.hotaku.media.model

import android.os.Parcelable
import com.hotaku.media.utils.MediaType
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumUi(
    val displayName: String,
    val thumbnailUriString: String,
    val thumbnailType: MediaType,
    val count: Int,
) : Parcelable
