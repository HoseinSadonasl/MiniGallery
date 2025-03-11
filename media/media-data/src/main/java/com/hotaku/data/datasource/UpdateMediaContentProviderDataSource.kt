package com.hotaku.data.datasource

interface UpdateMediaContentProviderDataSource {
    fun renameMedia(
        mediaUriString: String,
        name: String,
    ): Boolean
}
