package com.hotaku.data.mappers

import com.hotaku.common.mapper.Mapper
import com.hotaku.data.modes.AlbumData
import com.hotaku.media_domain.models.Album
import javax.inject.Inject

internal class MapAlbumDataAsAlbum
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
