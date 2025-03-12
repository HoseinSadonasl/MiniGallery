package com.hotaku.data.datasource

import com.hotaku.data.modes.MediaData

interface ContentProviderDataSource {
    fun getMedia(): List<MediaData>
}
