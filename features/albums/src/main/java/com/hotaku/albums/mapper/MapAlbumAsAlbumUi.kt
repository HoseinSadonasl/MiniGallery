package com.hotaku.albums.mapper

import com.hotaku.albums.model.AlbumUi
import com.hotaku.common.mapper.Mapper
import com.hotaku.media_domain.models.Album
import com.hotaku.ui.asMediaType
import javax.inject.Inject

internal class MapAlbumAsAlbumUi
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
