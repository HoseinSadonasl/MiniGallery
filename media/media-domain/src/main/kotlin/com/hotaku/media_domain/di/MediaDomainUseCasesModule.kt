package com.hotaku.media_domain.di

import com.hotaku.media_domain.repository.MediaRepository
import com.hotaku.media_domain.repository.SyncMediaRepository
import com.hotaku.media_domain.usecase.GetMediaUseCase
import com.hotaku.media_domain.usecase.GetMediaUseCaseImpl
import com.hotaku.media_domain.usecase.SyncMediaUseCase
import com.hotaku.media_domain.usecase.SyncMediaUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object MediaDomainUseCasesModule {
    @Provides
    @Singleton
    fun providesSyncMediaUseCase(syncMediaRepository: SyncMediaRepository): SyncMediaUseCase =
        SyncMediaUseCaseImpl(
            syncMediaRepository = syncMediaRepository,
        )

    @Provides
    @Singleton
    fun providesMediaUseCase(mediaRepository: MediaRepository): GetMediaUseCase =
        GetMediaUseCaseImpl(
            mediaRepository = mediaRepository,
        )
}
