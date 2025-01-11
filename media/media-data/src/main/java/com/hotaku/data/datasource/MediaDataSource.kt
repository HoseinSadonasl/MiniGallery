package com.hotaku.data.datasource

import androidx.paging.PagingSource
import com.hotaku.data.model.MediaData

interface MediaDataSource {
    fun getMedia(
        mimeType: String,
        query: String,
    ): PagingSource<Int, MediaData>
}
