package com.hotaku.data.mapper

import com.hotaku.data.model.MediaData
import org.junit.Before
import org.junit.Test
import java.time.Instant

class MediaDataMapperTest {
    private lateinit var mediaData: MediaData
//    private lateinit var mediaDomain: Media

    @Before
    fun setUp() {
        initializeMediaModel()
    }

    @Test
    fun `mapping MediaData to MediaDomain returns correct data`() {
//        val mappedDomain = mediaData.asDomain()
//        assertThat(mappedDomain).isEqualTo(mediaDomain)
    }

    private fun initializeMediaModel() {
        mediaData =
            MediaData(
                mediaId = 1,
                path = "path",
                displayName = "Hosein Sadon",
                mimeType = "image/*",
                dateAdded = Instant.now().toEpochMilli(),
                dateModified = Instant.now().toEpochMilli(),
                size = 1024,
            )

//        mediaDomain = Media(
//            mediaId = 1,
//            path = "path",
//            displayName = "Hosein Sadon",
//            mimeType = "image/*",
//            dateAdded = Instant.now().toEpochMilli(),
//            dateModified = Instant.now().toEpochMilli(),
//            size = 1024,
//        )
    }
}
