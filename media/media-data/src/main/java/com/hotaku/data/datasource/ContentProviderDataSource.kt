package com.hotaku.data.datasource

import com.hotaku.data.model.MediaData

interface ContentProviderDataSource {
    fun getMedia(): List<MediaData>
}
