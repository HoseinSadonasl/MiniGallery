package com.hotaku.media_domain.repository

interface ProviderRepository {
    suspend fun updateMediaDatabase(): Result<Int>
}
