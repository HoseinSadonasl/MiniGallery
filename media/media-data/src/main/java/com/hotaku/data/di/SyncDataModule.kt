package com.hotaku.data.di

import android.content.Context
import com.hotaku.common.di.Dispatcher
import com.hotaku.common.di.MiniGalleryDispatchers
import com.hotaku.data.repository.SyncMediaRepositoryImpl
import com.hotaku.datastore.MediaGenerationDataSource
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
internal object SyncDataModule {
    @Provides
    @Singleton
    fun providesSyncMediaRepository(
        @ApplicationContext context: Context,
        mediaGenerationDataSource: MediaGenerationDataSource,
        @Dispatcher(MiniGalleryDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    ): SyncMediaRepository =
        SyncMediaRepositoryImpl(
            context = context,
            mediaGenerationDataSource = mediaGenerationDataSource,
            coroutineDispatcher = ioDispatcher,
        )
}
