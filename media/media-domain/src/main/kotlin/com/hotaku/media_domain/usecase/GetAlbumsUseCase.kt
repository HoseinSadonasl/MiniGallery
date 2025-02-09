package com.hotaku.media_domain.usecase

import com.hotaku.domain.utils.DataResult
import com.hotaku.domain.utils.Error
import com.hotaku.media_domain.model.Album
import kotlinx.coroutines.flow.Flow

interface GetAlbumsUseCase {
    fun invoke(): Flow<DataResult<List<Album>, Error>>
}
