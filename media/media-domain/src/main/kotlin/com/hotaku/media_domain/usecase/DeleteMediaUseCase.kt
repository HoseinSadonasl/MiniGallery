package com.hotaku.media_domain.usecase

import com.hotaku.media_domain.models.Media

interface DeleteMediaUseCase {
    suspend operator fun invoke(media: List<Media>): Boolean
}
