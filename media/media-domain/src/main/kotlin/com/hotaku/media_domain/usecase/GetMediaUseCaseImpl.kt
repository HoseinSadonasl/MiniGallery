package com.hotaku.media_domain.usecase

import androidx.paging.PagingData
import com.hotaku.media_domain.models.Media
import com.hotaku.media_domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetMediaUseCaseImpl
    @Inject
    constructor(
        private val mediaRepository: MediaRepository,
    ) : GetMediaUseCase {
        override operator fun invoke(
            initialKey: Int,
            mimeType: String,
            query: String,
            albumName: String,
            matchTrash: Boolean,
            matchFavorite: Boolean,
        ): Flow<PagingData<Media>> =
            mediaRepository.getMedia(
                initialKey = initialKey,
                mimeType = mimeType,
                query = query,
                albumName = albumName,
                matchTrash = matchTrash,
                matchFavorite = matchFavorite,
            )
    }
