package com.hotaku.data.repository

import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class MediaRepositoryImplTest {
    private lateinit var mediaRepository: MediaRepositoryImpl

    @Before
    fun setUp() {
        mediaRepository = mockk()
    }

    @Test
    fun getting_media_return_success() {
    }
}
