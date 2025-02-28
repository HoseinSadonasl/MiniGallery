package com.hotaku.datastore.di

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val MINI_GALLERY_APP_PREFERENCES: String = "mini_gallery_app_preferences"

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
        produceFile = { context.preferencesDataStoreFile(MINI_GALLERY_APP_PREFERENCES) },
    )
}
