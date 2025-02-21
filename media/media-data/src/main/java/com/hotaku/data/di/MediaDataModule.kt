package com.hotaku.data.di

import com.hotaku.data.datasource.MediaDataSource
import com.hotaku.data.mapper.MapMediaAsMediaData
import com.hotaku.data.mapper.MapMediaDataAsMedia
import com.hotaku.data.repository.MediaRepositoryImpl
import com.hotaku.media_domain.repository.MediaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object MediaDataModule {
    @Provides
    @Singleton
    fun providesMediaRepository(
        mediaDataSource: MediaDataSource,
        mediaAsDomain: MapMediaDataAsMedia,
        mapMediaAsMediaData: MapMediaAsMediaData = MapMediaAsMediaData(),
    ): MediaRepository =
        MediaRepositoryImpl(
            mediaDataSource = mediaDataSource,
            mapMediaDataAsMedia = mediaAsDomain,
            mapMediaAsMediaData = mapMediaAsMediaData,
        )
}
