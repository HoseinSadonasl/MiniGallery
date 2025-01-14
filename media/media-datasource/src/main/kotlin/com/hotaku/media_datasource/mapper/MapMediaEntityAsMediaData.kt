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
                    path = path,
                    displayName = displayName,
                    mimeType = mimeType,
                    dateAdded = dateAdded.toLong(),
                    dateModified = dateModified.toLong(),
                    size = size.toLong(),
                )
            }
        }
    }
