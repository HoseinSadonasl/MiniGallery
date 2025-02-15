package com.hotaku.media_domain.usecase

import com.hotaku.media_domain.model.Media
import com.hotaku.media_domain.repository.MediaRepository
import javax.inject.Inject

internal class UpdateMediaUseCaseImpl
    @Inject
    constructor(
        private val mediaRepository: MediaRepository,
    ) : UpdateMediaUseCase {
        override suspend fun invoke(media: Media) = mediaRepository.updateMedia(media = media)
    }
