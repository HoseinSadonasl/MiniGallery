package com.hotaku.datastore

import androidx.datastore.preferences.core.intPreferencesKey

internal object AppPreferencesKeys {
    const val MINI_GALLERY_APP_PREFERENCES: String = "mini_gallery_app_preferences"
    val MEDIA_DATABASE_GENERATION = intPreferencesKey("MediaDataBaseGeneration")
}
