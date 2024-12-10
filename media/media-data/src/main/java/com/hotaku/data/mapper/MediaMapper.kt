package com.hotaku.data.mapper

import com.hotaku.data.model.MediaData
import com.hotaku.media_domain.model.Media

fun MediaData.asDomain(): Media {
    return Media(
        mediaId = mediaId,
        path = path,
        displayName = displayName,
        mimeType = mimeType,
        dateAdded = dateAdded,
        dateModified = dateModified,
        size = size,
    )
}
