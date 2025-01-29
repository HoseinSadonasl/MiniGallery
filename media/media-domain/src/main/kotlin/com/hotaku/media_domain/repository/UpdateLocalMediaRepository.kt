package com.hotaku.media_domain.repository

interface UpdateLocalMediaRepository {
    suspend fun update(): Result<Int>
}
