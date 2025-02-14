package com.hotaku.data.datasource

import com.hotaku.data.model.MediaData

interface ProviderDataSource {
    fun getMedia(): Result<List<MediaData>>

    fun updateMedia(media: MediaData): Result<Boolean>

    fun deleteMediaByUri(mediaUriString: String): Result<Boolean>
}
