package com.hotaku.data.mapper

import com.hotaku.common.mapper.Mapper
import com.hotaku.data.model.MediaData
import com.hotaku.media_domain.model.Media
import javax.inject.Inject

internal class MapMediaAsData
    @Inject
    constructor() : Mapper<Media, MediaData> {
        override fun map(from: Media): MediaData {
            with(from) {
                return MediaData(
                    mediaId = mediaId,
                    path = path,
                    displayName = displayName,
                    mimeType = mimeType,
                    duration = duration,
                    dateAdded = dateAdded,
                    dateModified = dateModified,
                    size = size,
                )
            }
        }
    }
