package com.hotaku.media.mapper

import com.hotaku.common.mapper.Mapper
import com.hotaku.media.model.MediaUi
import com.hotaku.media_domain.model.Media
import javax.inject.Inject

internal class MapMediaUiAsMedia
    @Inject
    constructor() : Mapper<MediaUi, Media> {
        override fun map(from: MediaUi): Media {
            with(from) {
                return Media(
                    mediaId = mediaId,
                    uriString = uriString,
                    displayName = displayName,
                    mimeType = mimeType.name,
                    duration = duration.toString(),
                    dateAdded = dateAdded.toEpochMilli(),
                    dateModified = dateModified.toEpochMilli(),
                    size = size,
                    bucketDisplayName = bucketDisplayName,
                )
            }
        }
    }
