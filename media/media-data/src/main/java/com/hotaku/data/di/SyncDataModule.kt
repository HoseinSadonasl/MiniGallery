package com.hotaku.data.di

import android.content.Context
import com.hotaku.data.repository.SyncMediaRepositoryImpl
import com.hotaku.media_domain.repository.SyncMediaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object SyncDataModule {
    @Provides
    @Singleton
    fun providesSyncMediaRepository(
        @ApplicationContext context: Context,
    ): SyncMediaRepository =
        SyncMediaRepositoryImpl(
            context = context,
        )
}
