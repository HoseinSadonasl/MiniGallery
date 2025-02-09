package com.hotaku.media_domain.usecase

import com.hotaku.domain.utils.DataResult
import com.hotaku.domain.utils.Error
import com.hotaku.domain.utils.executeFlowResult
import com.hotaku.media_domain.model.Album
import com.hotaku.media_domain.repository.AlbumsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetAlbumsUseCaseImpl
    @Inject
    constructor(
        private val albumsRepository: AlbumsRepository,
    ) : GetAlbumsUseCase {
        override fun invoke(): Flow<DataResult<List<Album>, Error>> =
            flow {
                val albums = albumsRepository.getAlbums()
                emit(DataResult.Success(albums))
            }.executeFlowResult()
    }
