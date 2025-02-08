package com.hotaku.media_domain.di

import com.hotaku.media_domain.repository.MediaRepository
import com.hotaku.media_domain.usecase.GetMediaUseCase
import com.hotaku.media_domain.usecase.GetMediaUseCaseImpl
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
}
