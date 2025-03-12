package com.hotaku.ui.mappers

import com.hotaku.common.mapper.Mapper
import com.hotaku.media_domain.models.Media
import com.hotaku.ui.models.MediaUi
import javax.inject.Inject

class MapMediaUiAsMedia
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
                    isTrash = isTrash,
                    isFavorite = isFavorite,
                    bucketDisplayName = bucketDisplayName,
                )
            }
        }
    }
