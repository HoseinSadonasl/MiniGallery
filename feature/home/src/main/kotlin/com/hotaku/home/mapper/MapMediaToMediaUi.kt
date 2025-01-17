package com.hotaku.home.mapper

import com.hotaku.common.mapper.Mapper
import com.hotaku.home.model.MediaUi
import com.hotaku.media_domain.model.Media
import java.time.Instant
import javax.inject.Inject

internal class MapMediaToMediaUi
    @Inject
    constructor() : Mapper<Media, MediaUi> {
        override fun map(from: Media): MediaUi {
            with(from) {
                return MediaUi(
                    mediaId = mediaId,
                    uriString = uriString,
                    displayName = displayName,
                    mimeType = mimeType,
                    duration = duration,
                    dateAdded = Instant.ofEpochMilli(dateAdded),
                    dateModified = Instant.ofEpochMilli(dateModified),
                    size = size,
                )
            }
        }
    }
