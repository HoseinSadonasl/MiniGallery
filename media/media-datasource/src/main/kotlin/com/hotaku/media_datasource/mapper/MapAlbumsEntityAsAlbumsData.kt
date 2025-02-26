package com.hotaku.media_datasource.mapper

import com.hotaku.common.mapper.Mapper
import com.hotaku.data.model.AlbumData
import com.hotaku.database.entity.AlbumEntity
import javax.inject.Inject

internal class MapAlbumsEntityAsAlbumsData
    @Inject
    constructor() : Mapper<AlbumEntity, AlbumData> {
        override fun map(from: AlbumEntity): AlbumData {
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
