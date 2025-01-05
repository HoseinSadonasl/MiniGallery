package com.hotaku.data.di

import androidx.work.WorkManager
import com.hotaku.data.datasource.MediaDataSource
import com.hotaku.data.datasource.ProviderDataSource
import com.hotaku.data.datasource.UpdateMediaDbDataSource
import com.hotaku.data.mapper.MapMediaAsDomain
import com.hotaku.data.repository.MediaRepositoryImpl
import com.hotaku.data.repository.ProviderRepositoryImpl
import com.hotaku.media_domain.repository.MediaRepository
import com.hotaku.media_domain.repository.ProviderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {
    @Provides
    @Singleton
    fun providesMediaRepository(
        mediaDataSource: MediaDataSource,
        mediaAsDomain: MapMediaAsDomain,
        workManager: WorkManager,
    ): MediaRepository =
        MediaRepositoryImpl(
            mediaDataSource = mediaDataSource,
            mediaAsDomain = mediaAsDomain,
            workManager = workManager,
        )

    @Provides
    @Singleton
    fun providesProviderRepository(
        updateMediaDbDataSource: UpdateMediaDbDataSource,
        providerDataSource: ProviderDataSource,
    ): ProviderRepository =
        ProviderRepositoryImpl(
            updateMediaDbDataSource = updateMediaDbDataSource,
            providerDataSource = providerDataSource,
        )
}
