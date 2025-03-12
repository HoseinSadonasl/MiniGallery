package com.hotaku.media_datasource.mappers

import com.hotaku.common.mapper.Mapper
import com.hotaku.data.modes.MediaData
import com.hotaku.media_datasource.entities.MediaEntity
import javax.inject.Inject

internal class MapMediaEntityAsMediaData
    @Inject
    constructor() : Mapper<MediaEntity, MediaData> {
        override fun map(from: MediaEntity): MediaData {
            with(from) {
                return MediaData(
                    mediaId = mediaId.toLong(),
                    uriString = uriString,
                    displayName = displayName,
                    mimeType = mimeType,
                    duration = duration,
                    dateAdded = dateAdded.toLong(),
                    dateModified = dateModified.toLong(),
                    size = size.toLong(),
                    isTrash = isTrash,
                    isFavorite = isFavorite,
                    bucketDisplayName = bucketDisplayName,
                )
            }
        }
    }
