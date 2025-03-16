package com.hotaku.media_domain.usecase

import com.hotaku.media_domain.models.Media

interface FavoriteMediaUseCase {
    suspend operator fun invoke(media: Media): Boolean
}
