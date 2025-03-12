package com.hotaku.media_datasource

import android.content.ContentResolver
import com.hotaku.data.datasource.ContentProviderDataSource
import com.hotaku.data.modes.MediaData
import com.hotaku.media_datasource.content_provider.MediaQueryUtils
import com.hotaku.media_datasource.content_provider.queryMediaFromContentProvider
import com.hotaku.media_datasource.mappers.MapMediaDtoAsMediaData
import javax.inject.Inject

internal class ContentProviderDataSourceImpl
    @Inject
    constructor(
        private val contentResolver: ContentResolver,
        private val mapMediaDtoAsMediaData: MapMediaDtoAsMediaData,
    ) : ContentProviderDataSource {
        override fun getMedia(): List<MediaData> =
            contentResolver.queryMediaFromContentProvider(
                uri = MediaQueryUtils.MediaStoreFileUri,
                projection = MediaQueryUtils.MediaProjection,
            ).map { mapMediaDtoAsMediaData.map(it) }
    }
