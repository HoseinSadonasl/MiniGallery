package com.hotaku.media_domain.usecase

import com.hotaku.media_domain.models.Media

interface RenameMediaUseCase {
    suspend operator fun invoke(media: Media): Boolean
}
