package com.hotaku.media_domain.di

import com.hotaku.media_domain.repository.SyncMediaRepository
import com.hotaku.media_domain.usecase.SyncMediaUseCase
import com.hotaku.media_domain.usecase.SyncMediaUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object SyncDomainModule {
    @Provides
    @Singleton
    fun providesSyncMediaUseCase(syncMediaRepository: SyncMediaRepository): SyncMediaUseCase =
        SyncMediaUseCaseImpl(
            syncMediaRepository = syncMediaRepository,
        )
}
