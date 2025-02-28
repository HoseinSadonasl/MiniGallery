package com.hotaku.datastore

interface MediaGenerationDataSource {
    suspend fun getMediaGeneration(): Int

    suspend fun setMediaGeneration(generation: Int)
}
