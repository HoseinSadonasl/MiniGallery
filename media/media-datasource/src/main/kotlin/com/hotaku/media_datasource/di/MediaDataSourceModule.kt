package com.hotaku.media_datasource.di

import com.hotaku.data.datasource.MediaDataSource
import com.hotaku.database.dao.MediaDao
import com.hotaku.media_datasource.MediaDataSourceImpl
import com.hotaku.media_datasource.mapper.MapMediaEntityAsMediaData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object MediaDataSourceModule {
    @Provides
    @Singleton
    fun providesMapMediaEntityAsMediaData(): MapMediaEntityAsMediaData = MapMediaEntityAsMediaData()

    @Provides
    @Singleton
    fun providesMediaDataSource(
        mapMediaEntityAsMediaData: MapMediaEntityAsMediaData,
        mediaDao: MediaDao,
    ): MediaDataSource =
        MediaDataSourceImpl(
            mapMediaEntityAsMediaData = mapMediaEntityAsMediaData,
            mediaDao = mediaDao,
        )
}