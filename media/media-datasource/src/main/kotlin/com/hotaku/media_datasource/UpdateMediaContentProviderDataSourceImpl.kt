package com.hotaku.media_datasource

import android.content.ContentResolver
import com.hotaku.data.datasource.UpdateMediaContentProviderDataSource
import com.hotaku.media_datasource.content_provider.markMediaAsFavorite
import com.hotaku.media_datasource.content_provider.renameMedia
import javax.inject.Inject

internal class UpdateMediaContentProviderDataSourceImpl
    @Inject
    constructor(
        private val contentResolver: ContentResolver,
    ) : UpdateMediaContentProviderDataSource {
        override fun renameMedia(
            mediaUriString: String,
            name: String,
        ): Boolean =
            contentResolver.renameMedia(
                mediaUriString = mediaUriString,
                name = name,
            )

        override fun markMedaAsFavorite(
            mediaUriString: String,
            isFavorite: Boolean,
        ): Boolean =
            contentResolver.markMediaAsFavorite(
                mediaUriString = mediaUriString,
                isFavorite = isFavorite,
            )
    }
