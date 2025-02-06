package com.hotaku.home.mapper

import com.hotaku.common.mapper.Mapper
import com.hotaku.home.model.MediaUi
import com.hotaku.home.utils.MediaType
import com.hotaku.home.utils.TimeUtils.millisAsFormattedDuration
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
                    mimeType = mimeType.asMediaType(),
                    duration = duration.toInt().millisAsFormattedDuration(),
                    dateAdded = Instant.ofEpochMilli(dateAdded),
                    dateModified = Instant.ofEpochMilli(dateModified),
                    size = size,
                )
            }
        }

        private fun String.asMediaType(): MediaType {
            return when {
                this.startsWith("video") -> MediaType.VIDEO
                this.startsWith("image") -> MediaType.IMAGE
                else -> MediaType.UNKNOWN
            }
        }
    }
