package com.hotaku.media_datasource.di

import android.content.ContentResolver
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ProviderModule {
    @Provides
    @Singleton
    fun providesContentResolver(
        @ApplicationContext appContext: Context,
    ): ContentResolver = appContext.contentResolver
}
