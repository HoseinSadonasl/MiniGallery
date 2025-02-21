package com.hotaku.media_domain.usecase

import com.hotaku.media_domain.model.Media
import com.hotaku.media_domain.repository.MediaRepository
import javax.inject.Inject

internal class DeleteMediaUseCaseImpl
    @Inject
    constructor(
        private val mediaRepository: MediaRepository,
    ) : DeleteMediaUseCase {
        override suspend fun invoke(media: List<Media>) =
            mediaRepository.deleteMedia(
                media = media,
            )
    }
