package com.hotaku.media_datasource.mapper

import com.hotaku.common.mapper.Mapper
import com.hotaku.data.model.MediaData
import com.hotaku.media_datasource.models.MediaDto
import javax.inject.Inject

internal class MapMediaDtoAsMediaData
    @Inject
    constructor() : Mapper<MediaDto, MediaData> {
        override fun map(from: MediaDto): MediaData {
            with(from) {
                return MediaData(
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
