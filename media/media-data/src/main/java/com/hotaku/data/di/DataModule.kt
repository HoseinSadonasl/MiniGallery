package com.hotaku.data.di

import android.content.Context
import com.hotaku.common.di.Dispatcher
import com.hotaku.common.di.MiniGalleryDispatchers
import com.hotaku.data.datasource.MediaDataSource
import com.hotaku.data.datasource.ProviderDataSource
import com.hotaku.data.datasource.UpdateMediaDbDataSource
import com.hotaku.data.mapper.MapMediaAsDomain
import com.hotaku.data.repository.MediaRepositoryImpl
import com.hotaku.data.repository.ProviderRepositoryImpl
import com.hotaku.data.repository.SyncMediaRepositoryImpl
import com.hotaku.media_domain.repository.MediaRepository
import com.hotaku.media_domain.repository.ProviderRepository
import com.hotaku.media_domain.repository.SyncMediaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {
    @Provides
    @Singleton
    fun providesSyncMediaRepository(
        @ApplicationContext context: Context,
        @Dispatcher(MiniGalleryDispatchers.IO) coroutineDispatcher: CoroutineDispatcher,
    ): SyncMediaRepository =
        SyncMediaRepositoryImpl(
            context = context,
            coroutineDispatcher = coroutineDispatcher,
        )

    @Provides
    @Singleton
    fun providesMediaRepository(
        mediaDataSource: MediaDataSource,
        mediaAsDomain: MapMediaAsDomain,
    ): MediaRepository =
        MediaRepositoryImpl(
            mediaDataSource = mediaDataSource,
            mediaAsDomain = mediaAsDomain,
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
