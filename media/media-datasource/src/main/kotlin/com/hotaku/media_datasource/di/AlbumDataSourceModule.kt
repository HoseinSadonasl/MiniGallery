package com.hotaku.media_datasource.di

import com.hotaku.data.datasource.AlbumsDataSource
import com.hotaku.media_datasource.AlbumsDataSourceImpl
import com.hotaku.media_datasource.dao.MediaDao
import com.hotaku.media_datasource.mapper.MapAlbumsEntityAsAlbumsData
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
        mapAlbumsEntityAsAlbumsData: MapAlbumsEntityAsAlbumsData = MapAlbumsEntityAsAlbumsData(),
    ): AlbumsDataSource =
        AlbumsDataSourceImpl(
            mediaDao = mediaDao,
            mapAlbumsEntityAsAlbumsData = mapAlbumsEntityAsAlbumsData,
        )
}
