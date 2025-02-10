package com.hotaku.media_datasource

import com.hotaku.data.datasource.AlbumsDataSource
import com.hotaku.data.model.AlbumData
import com.hotaku.database.dao.MediaDao
import com.hotaku.media_datasource.mapper.MapAlbumsDtoAsAlbumsData
import javax.inject.Inject

internal class AlbumsDataSourceImpl
    @Inject
    constructor(
        private val mediaDao: MediaDao,
        private val mapAlbumsDtoAsAlbumsData: MapAlbumsDtoAsAlbumsData,
    ) : AlbumsDataSource {
        override suspend fun getAlbums(): List<AlbumData> = mediaDao.getAlbums().map { mapAlbumsDtoAsAlbumsData.map(it) }
    }
