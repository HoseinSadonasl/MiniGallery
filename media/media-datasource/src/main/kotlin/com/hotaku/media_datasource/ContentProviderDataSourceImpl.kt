package com.hotaku.media_datasource

import android.content.ContentResolver
import com.hotaku.data.datasource.ContentProviderDataSource
import com.hotaku.data.model.MediaData
import com.hotaku.media_datasource.mapper.MapMediaDtoAsMediaData
import com.hotaku.media_datasource.utils.MediaQueryUtils
import com.hotaku.media_datasource.utils.queryMediaFromContentProvider
import javax.inject.Inject

internal class ContentProviderDataSourceImpl
    @Inject
    constructor(
        private val contentResolver: ContentResolver,
        private val mapMediaDtoAsMediaData: MapMediaDtoAsMediaData,
    ) : ContentProviderDataSource {
        override fun getMedia(): Result<List<MediaData>> =
            contentResolver.queryMediaFromContentProvider(
                uri = MediaQueryUtils.MediaStoreFileUri,
                projection = MediaQueryUtils.MediaProjection,
            ).map { it.map { mapMediaDtoAsMediaData.map(it) } }
    }
