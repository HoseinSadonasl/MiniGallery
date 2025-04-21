package com.hotaku.media_domain.di

import com.hotaku.media_domain.repository.MediaRepository
import com.hotaku.media_domain.usecase.DeleteMediaUseCase
import com.hotaku.media_domain.usecase.DeleteMediaUseCaseImpl
import com.hotaku.media_domain.usecase.FavoriteMediaUseCase
import com.hotaku.media_domain.usecase.FavoriteMediaUseCaseImpl
import com.hotaku.media_domain.usecase.GetMediaUseCase
import com.hotaku.media_domain.usecase.GetMediaUseCaseImpl
import com.hotaku.media_domain.usecase.RenameMediaUseCase
import com.hotaku.media_domain.usecase.RenameMediaUseCaseImpl
import com.hotaku.media_domain.usecase.TrashMediaUseCase
import com.hotaku.media_domain.usecase.TrashMediaUseCaseImpl
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
    fun provideUpdateMediaUseCase(mediaRepository: MediaRepository): RenameMediaUseCase =
        RenameMediaUseCaseImpl(
            mediaRepository = mediaRepository,
        )

    @Provides
    @Singleton
    fun provideTrashMediaUseCase(mediaRepository: MediaRepository): TrashMediaUseCase =
        TrashMediaUseCaseImpl(
            mediaRepository = mediaRepository,
        )

    @Provides
    @Singleton
    fun provideFavoriteMediaUseCase(mediaRepository: MediaRepository): FavoriteMediaUseCase =
        FavoriteMediaUseCaseImpl(
            mediaRepository = mediaRepository,
        )

    @Provides
    @Singleton
    fun provideDeleteMediaUseCase(mediaRepository: MediaRepository): DeleteMediaUseCase =
        DeleteMediaUseCaseImpl(
            mediaRepository = mediaRepository,
        )
}
