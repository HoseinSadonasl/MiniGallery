package com.hotaku.media_datasource.mapper

import com.hotaku.common.mapper.Mapper
import com.hotaku.data.model.AlbumData
import com.hotaku.database.entity.AlbumDto
import javax.inject.Inject

internal class MapAlbumsDtoAsAlbumsData
    @Inject
    constructor() : Mapper<AlbumDto, AlbumData> {
        override fun map(from: AlbumDto): AlbumData {
            with(from) {
                return AlbumData(
                    displayName = displayName,
                    thumbnailUriString = thumbnailUriString,
                    thumbnailType = thumbnailType,
                    count = count,
                )
            }
        }
    }
