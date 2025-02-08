package com.hotaku.media_domain.usecase

import com.hotaku.media_domain.model.Album

interface GetAlbumsUseCase {
    suspend fun invoke(): List<Album>
}
