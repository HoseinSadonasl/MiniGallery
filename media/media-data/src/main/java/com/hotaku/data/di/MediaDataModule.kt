package com.hotaku.data.di

import com.hotaku.data.datasource.MediaDataSource
import com.hotaku.data.datasource.ProviderDataSource
import com.hotaku.data.mapper.MapMediaAsData
import com.hotaku.data.mapper.MapMediaAsDomain
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
        mediaAsDomain: MapMediaAsDomain,
        providerDataSource: ProviderDataSource,
        mapMediaAsData: MapMediaAsData = MapMediaAsData(),
    ): MediaRepository =
        MediaRepositoryImpl(
            mediaDataSource = mediaDataSource,
            mediaAsDomain = mediaAsDomain,
            providerDataSource = providerDataSource,
            mapMediaAsData = mapMediaAsData,
        )
}
