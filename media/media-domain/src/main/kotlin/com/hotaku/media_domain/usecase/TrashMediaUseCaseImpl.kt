package com.hotaku.media_domain.usecase

import com.hotaku.media_domain.model.Media
import com.hotaku.media_domain.repository.MediaRepository
import javax.inject.Inject

internal class TrashMediaUseCaseImpl
    @Inject
    constructor(
        private val mediaRepository: MediaRepository,
    ) : TrashMediaUseCase {
        override suspend fun invoke(media: List<Media>): Boolean =
            mediaRepository.trashMedia(
                media = media,
            )
    }
