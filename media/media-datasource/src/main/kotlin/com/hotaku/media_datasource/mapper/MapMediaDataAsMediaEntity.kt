package com.hotaku.media_datasource.mapper

import com.hotaku.common.mapper.Mapper
import com.hotaku.data.model.MediaData
import com.hotaku.database.entity.MediaEntity
import javax.inject.Inject

internal class MapMediaDataAsMediaEntity
    @Inject
    constructor() : Mapper<MediaData, MediaEntity> {
        override fun map(from: MediaData): MediaEntity {
            with(from) {
                return MediaEntity(
                    mediaId = mediaId.toString(),
                    path = path,
                    displayName = displayName,
                    mimeType = mimeType,
                    dateAdded = dateAdded.toString(),
                    dateModified = dateModified.toString(),
                    size = size.toString(),
                )
            }
        }
    }
