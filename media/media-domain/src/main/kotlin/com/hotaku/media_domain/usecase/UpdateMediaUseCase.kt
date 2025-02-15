package com.hotaku.media_domain.usecase

import com.hotaku.media_domain.model.Media

interface UpdateMediaUseCase {
    suspend operator fun invoke(media: Media)
}
