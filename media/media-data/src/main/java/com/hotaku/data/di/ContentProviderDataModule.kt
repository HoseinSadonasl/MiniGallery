package com.hotaku.data.di

import com.hotaku.data.datasource.ContentProviderDataSource
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
internal object ContentProviderDataModule {
    @Provides
    @Singleton
    fun providesContentProviderRepository(
        updateMediaDbDataSource: UpdateMediaDbDataSource,
        contentProviderDataSource: ContentProviderDataSource,
    ): UpdateLocalMediaRepository =
        UpdateLocalLocalMediaRepositoryImpl(
            updateMediaDbDataSource = updateMediaDbDataSource,
            contentProviderDataSource = contentProviderDataSource,
        )
}
