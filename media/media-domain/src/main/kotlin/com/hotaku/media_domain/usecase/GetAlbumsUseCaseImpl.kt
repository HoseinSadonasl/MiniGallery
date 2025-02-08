package com.hotaku.media_domain.usecase

import com.hotaku.media_domain.model.Album
import com.hotaku.media_domain.repository.AlbumsRepository
import javax.inject.Inject

internal class GetAlbumsUseCaseImpl
    @Inject
    constructor(
        private val albumsRepository: AlbumsRepository,
    ) : GetAlbumsUseCase {
        override suspend fun invoke(): List<Album> = albumsRepository.getAlbums()
    }
