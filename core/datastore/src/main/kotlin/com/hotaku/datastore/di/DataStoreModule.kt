package com.hotaku.datastore.di

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.hotaku.datastore.AppPreferencesKeys.MINI_GALLERY_APP_PREFERENCES
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {
    @Provides
    @Singleton
    fun providesDataStorePreferences(
        @ApplicationContext context: Context,
    ) = PreferenceDataStoreFactory.create(
        corruptionHandler =
            ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() },
            ),
        produceFile = { context.preferencesDataStoreFile(name = MINI_GALLERY_APP_PREFERENCES) },
    )
}
