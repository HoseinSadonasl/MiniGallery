package com.hotaku.data.mappers

import com.hotaku.common.mapper.Mapper
import com.hotaku.data.modes.MediaData
import com.hotaku.media_domain.models.Media
import javax.inject.Inject

internal class MapMediaDataAsMedia
    @Inject
    constructor() : Mapper<MediaData, Media> {
        override fun map(from: MediaData): Media {
            with(from) {
                return Media(
                    mediaId = mediaId,
                    uriString = uriString,
                    displayName = displayName,
                    mimeType = mimeType,
                    duration = duration,
                    dateAdded = dateAdded,
                    dateModified = dateModified,
                    size = size,
                    isTrash = isTrash,
                    isFavorite = isFavorite,
                    bucketDisplayName = bucketDisplayName,
                )
            }
        }
    }
