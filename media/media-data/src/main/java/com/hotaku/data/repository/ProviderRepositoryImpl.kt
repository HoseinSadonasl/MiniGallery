package com.hotaku.data.repository

import com.hotaku.data.datasource.ProviderDataSource
import com.hotaku.data.datasource.UpdateMediaDbDataSource
import com.hotaku.media_domain.repository.ProviderRepository
import javax.inject.Inject

internal class ProviderRepositoryImpl
    @Inject
    constructor(
        private val updateMediaDbDataSource: UpdateMediaDbDataSource,
        private val providerDataSource: ProviderDataSource,
    ) : ProviderRepository {
        override suspend fun updateMediaDatabase(): Result<Int> =
            runCatching {
                val providerMedia = providerDataSource.getMedia().getOrThrow()
                updateMediaDbDataSource.saveMedia(providerMedia)
                providerMedia.size
            }
    }
