package com.hotaku.media_domain.di

import com.hotaku.media_domain.repository.MediaRepository
import com.hotaku.media_domain.usecase.DeleteMediaUseCase
import com.hotaku.media_domain.usecase.DeleteMediaUseCaseImpl
import com.hotaku.media_domain.usecase.GetMediaUseCase
import com.hotaku.media_domain.usecase.GetMediaUseCaseImpl
import com.hotaku.media_domain.usecase.UpdateMediaUseCase
import com.hotaku.media_domain.usecase.UpdateMediaUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object MediaDomainModule {
    @Provides
    @Singleton
    fun providesMediaUseCase(mediaRepository: MediaRepository): GetMediaUseCase =
        GetMediaUseCaseImpl(
            mediaRepository = mediaRepository,
        )

    @Provides
    @Singleton
    fun provideUpdateMediaUseCase(mediaRepository: MediaRepository): UpdateMediaUseCase =
        UpdateMediaUseCaseImpl(
            mediaRepository = mediaRepository,
        )

    @Provides
    @Singleton
    fun provideDeleteMediaUseCase(mediaRepository: MediaRepository): DeleteMediaUseCase =
        DeleteMediaUseCaseImpl(
            mediaRepository = mediaRepository,
        )
}
