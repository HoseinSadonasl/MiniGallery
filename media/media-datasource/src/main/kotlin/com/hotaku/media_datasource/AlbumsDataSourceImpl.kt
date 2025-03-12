package com.hotaku.media_datasource

import com.hotaku.data.datasource.AlbumsDataSource
import com.hotaku.data.modes.AlbumData
import com.hotaku.media_datasource.dao.MediaDao
import com.hotaku.media_datasource.mappers.MapAlbumsEntityAsAlbumsData
import javax.inject.Inject

internal class AlbumsDataSourceImpl
    @Inject
    constructor(
        private val mediaDao: MediaDao,
        private val mapAlbumsEntityAsAlbumsData: MapAlbumsEntityAsAlbumsData,
    ) : AlbumsDataSource {
        override suspend fun getAlbums(): List<AlbumData> =
            mediaDao.getAlbums()
                .map { albumEntity -> mapAlbumsEntityAsAlbumsData.map(from = albumEntity) }
    }
