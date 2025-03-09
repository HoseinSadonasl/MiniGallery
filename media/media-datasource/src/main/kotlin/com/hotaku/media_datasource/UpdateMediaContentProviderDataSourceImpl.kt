package com.hotaku.media_datasource

import android.content.ContentResolver
import com.hotaku.data.datasource.UpdateMediaContentProviderDataSource
import com.hotaku.media_datasource.utils.renameMedia
import javax.inject.Inject

internal class UpdateMediaContentProviderDataSourceImpl
    @Inject
    constructor(
        private val contentResolver: ContentResolver,
    ) : UpdateMediaContentProviderDataSource {
        override fun renameMedia(
            mediaUriString: String,
            name: String,
        ): Result<Boolean> =
            contentResolver.renameMedia(
                mediaUriString = mediaUriString,
                name = name,
            )
    }
