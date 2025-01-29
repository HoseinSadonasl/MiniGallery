package com.hotaku.data.repository

import com.hotaku.data.datasource.ProviderDataSource
import com.hotaku.data.datasource.UpdateMediaDbDataSource
import com.hotaku.data.model.MediaData
import com.hotaku.media_domain.repository.UpdateLocalMediaRepository
import javax.inject.Inject

internal class UpdateLocalLocalMediaRepositoryImpl
    @Inject
    constructor(
        private val updateMediaDbDataSource: UpdateMediaDbDataSource,
        private val providerDataSource: ProviderDataSource,
    ) : UpdateLocalMediaRepository {
        override suspend fun update(): Result<Int> =
            runCatching {
                val providerMedia: List<MediaData> = providerDataSource.getMedia().getOrThrow()

                val newUris: List<String> = providerMedia.map { it.uriString }
                val dbUris: List<String> = updateMediaDbDataSource.getMediaStringUris()
                if (dbUris.isNotEmpty()) deleteNotLongerExistMedia(dbUris = dbUris, newUris = newUris)

                updateMediaDbDataSource.insertMediaToDatabase(media = providerMedia)
                providerMedia.size
            }

        private fun deleteNotLongerExistMedia(
            dbUris: List<String>,
            newUris: List<String>,
        ) {
            dbUris.filterNot { uri -> newUris.contains(uri) }.let { filteredUris ->
                filteredUris.isNotEmpty().let {
                    updateMediaDbDataSource.deleteNoExistMedia(uris = filteredUris)
                }
            }
        }
    }
