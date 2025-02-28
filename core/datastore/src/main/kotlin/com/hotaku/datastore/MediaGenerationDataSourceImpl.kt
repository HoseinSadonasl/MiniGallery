package com.hotaku.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.hotaku.datastore.AppKeys.MEDIA_DATABASE_GENERATION
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

internal class MediaGenerationDataSourceImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : MediaGenerationDataSource {
        override suspend fun getMediaGeneration(): Int = dataStore.data.firstOrNull()?.get(MEDIA_DATABASE_GENERATION) ?: 0

        override suspend fun setMediaGeneration(generation: Int) {
            dataStore.edit { preferences ->
                preferences[MEDIA_DATABASE_GENERATION] = generation
            }
        }
    }
