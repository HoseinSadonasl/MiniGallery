package com.hotaku.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.hotaku.datastore.MediaGenerationDataSource
import com.hotaku.datastore.MediaGenerationDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object MediaGenerationDataSourceModule {
    @Provides
    @Singleton
    fun providesMediaGenerationDataSource(dataStore: DataStore<Preferences>): MediaGenerationDataSource =
        MediaGenerationDataSourceImpl(
            dataStore = dataStore,
        )
}
