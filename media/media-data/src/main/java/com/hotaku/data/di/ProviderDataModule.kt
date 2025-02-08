package com.hotaku.data.di

import com.hotaku.data.datasource.ProviderDataSource
import com.hotaku.data.datasource.UpdateMediaDbDataSource
import com.hotaku.data.repository.UpdateLocalLocalMediaRepositoryImpl
import com.hotaku.media_domain.repository.UpdateLocalMediaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ProviderDataModule {
    @Provides
    @Singleton
    fun providesProviderRepository(
        updateMediaDbDataSource: UpdateMediaDbDataSource,
        providerDataSource: ProviderDataSource,
    ): UpdateLocalMediaRepository =
        UpdateLocalLocalMediaRepositoryImpl(
            updateMediaDbDataSource = updateMediaDbDataSource,
            providerDataSource = providerDataSource,
        )
}
