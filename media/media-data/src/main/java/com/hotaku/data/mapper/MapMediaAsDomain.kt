package com.hotaku.data.mapper

import com.hotaku.common.mapper.Mapper
import com.hotaku.data.model.MediaData
import com.hotaku.media_domain.model.Media
import javax.inject.Inject

internal class MapMediaAsDomain
    @Inject
    constructor() : Mapper<MediaData, Media> {
        override fun map(from: MediaData): Media {
            with(from) {
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
        }
    }
