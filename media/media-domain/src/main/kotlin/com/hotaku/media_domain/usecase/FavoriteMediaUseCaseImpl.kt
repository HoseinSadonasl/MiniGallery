package com.hotaku.media_domain.usecase

import com.hotaku.media_domain.models.Media
import com.hotaku.media_domain.repository.MediaRepository
import javax.inject.Inject

internal class FavoriteMediaUseCaseImpl
    @Inject
    constructor(
        private val mediaRepository: MediaRepository,
    ) : FavoriteMediaUseCase {
        override suspend fun invoke(media: Media): Boolean = mediaRepository.markMediaAsFavorite(media = media)
    }
