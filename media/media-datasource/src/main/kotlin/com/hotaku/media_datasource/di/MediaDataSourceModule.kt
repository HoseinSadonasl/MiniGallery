package com.hotaku.media_datasource.di

import android.content.ContentResolver
import com.hotaku.data.datasource.MediaDataSource
import com.hotaku.data.datasource.ProviderDataSource
import com.hotaku.data.datasource.UpdateMediaDbDataSource
import com.hotaku.database.dao.MediaDao
import com.hotaku.media_datasource.MediaDataSourceImpl
import com.hotaku.media_datasource.ProviderDataSourceImpl
import com.hotaku.media_datasource.UpdateMediaDataSourceImpl
import com.hotaku.media_datasource.mapper.MapMediaDataAsMediaEntity
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
    fun providesMediaDataSource(
        mapMediaEntityAsMediaData: MapMediaEntityAsMediaData,
        mediaDao: MediaDao,
    ): MediaDataSource =
        MediaDataSourceImpl(
            mapMediaEntityAsMediaData = mapMediaEntityAsMediaData,
            mediaDao = mediaDao,
        )

    @Provides
    @Singleton
    fun providesUpdateMediaDataSource(
        mapMediaDataAsMediaEntity: MapMediaDataAsMediaEntity,
        mediaDao: MediaDao,
    ): UpdateMediaDbDataSource =
        UpdateMediaDataSourceImpl(
            mapMediaDataAsMediaEntity = mapMediaDataAsMediaEntity,
            mediaDao = mediaDao,
        )

    @Provides
    @Singleton
    fun providesProviderDataSource(contentResolver: ContentResolver): ProviderDataSource =
        ProviderDataSourceImpl(
            contentResolver = contentResolver,
        )
}
