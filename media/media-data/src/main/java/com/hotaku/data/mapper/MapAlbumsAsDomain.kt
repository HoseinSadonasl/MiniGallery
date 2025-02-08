package com.hotaku.data.mapper

import com.hotaku.common.mapper.Mapper
import com.hotaku.data.model.AlbumData
import com.hotaku.media_domain.model.Album
import javax.inject.Inject

internal class MapAlbumsAsDomain
    @Inject
    constructor() : Mapper<AlbumData, Album> {
        override fun map(from: AlbumData): Album {
            with(from) {
                return Album(
                    displayName = displayName,
                    thumbnailUriString = thumbnailUriString,
                    thumbnailType = thumbnailType,
                    count = count,
                )
            }
        }
    }
