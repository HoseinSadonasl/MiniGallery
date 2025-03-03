package com.hotaku.ui.mappers

import com.hotaku.common.mapper.Mapper
import com.hotaku.media_domain.model.Media
import com.hotaku.ui.asMediaType
import com.hotaku.ui.models.MediaUi
import java.time.Instant
import javax.inject.Inject

class MapMediaAsMediaUi
    @Inject
    constructor() : Mapper<Media, MediaUi> {
        override fun map(from: Media): MediaUi {
            with(from) {
                return MediaUi(
                    mediaId = mediaId,
                    uriString = uriString,
                    displayName = displayName,
                    mimeType = mimeType.asMediaType(),
                    duration = duration.toInt(),
                    dateAdded = Instant.ofEpochMilli(dateAdded),
                    dateModified = Instant.ofEpochMilli(dateModified),
                    size = size,
                    bucketDisplayName = bucketDisplayName,
                )
            }
        }
    }
