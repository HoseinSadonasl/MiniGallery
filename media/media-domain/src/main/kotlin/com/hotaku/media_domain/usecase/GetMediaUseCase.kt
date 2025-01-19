package com.hotaku.media_domain.usecase

import androidx.paging.PagingData
import com.hotaku.media_domain.model.Media
import kotlinx.coroutines.flow.Flow

interface GetMediaUseCase {
    operator fun invoke(
        mimeType: String,
        query: String,
    ): Flow<PagingData<Media>>
}
