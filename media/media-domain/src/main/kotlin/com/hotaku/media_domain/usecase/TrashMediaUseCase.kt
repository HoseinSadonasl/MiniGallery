package com.hotaku.media_domain.usecase

import com.hotaku.media_domain.model.Media

interface TrashMediaUseCase {
    suspend operator fun invoke(media: List<Media>): Boolean
}
