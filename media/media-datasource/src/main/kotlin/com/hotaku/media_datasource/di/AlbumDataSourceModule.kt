package com.hotaku.media_datasource.di

import com.hotaku.data.datasource.AlbumsDataSource
import com.hotaku.database.dao.MediaDao
import com.hotaku.media_datasource.AlbumsDataSourceImpl
import com.hotaku.media_datasource.mapper.MapAlbumsDtoAsAlbumsData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AlbumDataSourceModule {
    @Provides
    @Singleton
    fun providesAlbumDataSource(
        mediaDao: MediaDao,
        mapAlbumsDtoAsAlbumsData: MapAlbumsDtoAsAlbumsData = MapAlbumsDtoAsAlbumsData(),
    ): AlbumsDataSource =
        AlbumsDataSourceImpl(
            mediaDao = mediaDao,
            mapAlbumsDtoAsAlbumsData = mapAlbumsDtoAsAlbumsData,
        )
}
