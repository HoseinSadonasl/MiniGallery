package com.hotaku.media_domain.usecase

import com.hotaku.media_domain.model.Media

interface DeleteMediaUseCase {
    suspend operator fun invoke(media: List<Media>)
}
