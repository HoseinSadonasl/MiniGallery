package com.hotaku.media_datasource

import android.content.ContentResolver
import com.hotaku.data.datasource.ProviderDataSource
import com.hotaku.data.model.MediaData
import com.hotaku.media_datasource.utils.MediaQueries
import com.hotaku.media_datasource.utils.queryMedia
import javax.inject.Inject

internal class ProviderDataSourceImpl
    @Inject
    constructor(
        private val contentResolver: ContentResolver,
    ) : ProviderDataSource {
        override fun getMedia(): Result<List<MediaData>> =
            contentResolver.queryMedia(
                uri = MediaQueries.MediaStoreFileUri,
                projection = MediaQueries.MediaProjection,
            )
    }
