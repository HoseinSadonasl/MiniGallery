package com.hotaku.media_domain.usecase

interface DeleteMediaUseCase {
    suspend operator fun invoke(mediaUriString: String)
}
