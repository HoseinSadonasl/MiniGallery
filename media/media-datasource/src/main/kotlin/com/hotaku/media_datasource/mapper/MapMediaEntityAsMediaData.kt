package com.hotaku.media_datasource.mapper

import com.hotaku.common.mapper.Mapper
import com.hotaku.data.model.MediaData
import com.hotaku.database.entity.MediaEntity
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
                    bucketDisplayName = bucketDisplayName,
                )
            }
        }
    }
