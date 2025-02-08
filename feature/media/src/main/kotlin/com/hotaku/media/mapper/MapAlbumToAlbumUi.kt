package com.hotaku.media.mapper

import com.hotaku.common.mapper.Mapper
import com.hotaku.media.model.AlbumUi
import com.hotaku.media.utils.asMediaType
import com.hotaku.media_domain.model.Album
import javax.inject.Inject

internal class MapAlbumToAlbumUi
    @Inject
    constructor() : Mapper<Album, AlbumUi> {
        override fun map(from: Album): AlbumUi {
            with(from) {
                return AlbumUi(
                    displayName = displayName,
                    thumbnailUriString = thumbnailUriString,
                    thumbnailType = thumbnailType.asMediaType(),
                    count = count,
                )
            }
        }
    }
