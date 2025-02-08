package com.hotaku.media_domain.di

import com.hotaku.media_domain.repository.AlbumsRepository
import com.hotaku.media_domain.usecase.GetAlbumsUseCase
import com.hotaku.media_domain.usecase.GetAlbumsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AlbumsDomainModule {
    @Provides
    @Singleton
    fun providesAlbumsRepository(
        albumsRepository: AlbumsRepository,
    ): GetAlbumsUseCase =
        GetAlbumsUseCaseImpl(
            albumsRepository = albumsRepository,
        )
}
