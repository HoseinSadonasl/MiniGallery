package com.hotaku.media_domain.usecase

import androidx.paging.PagingData
import com.hotaku.media_domain.model.Media
import com.hotaku.media_domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetMediaUseCaseImpl
    @Inject
    constructor(
        private val mediaRepository: MediaRepository,
    ) : GetMediaUseCase {
        override suspend fun invoke(
            mimeType: String,
            query: String,
        ): Flow<PagingData<Media>> =
            mediaRepository.getMedia(
                mimeType = mimeType,
                query = query,
            )
    }
