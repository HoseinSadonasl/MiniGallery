package com.hotaku.data.mapper

import com.google.common.truth.Truth.assertThat
import com.hotaku.data.model.MediaData
import com.hotaku.media_domain.model.Media
import org.junit.Test
import java.time.Instant

internal class MediaMapperTest {
    val timeInMillis = Instant.now().toEpochMilli()

    @Test
    fun `mapping MediaData to MediaDomain returns correct data`() {
        val mapMediaAsDomain = MapMediaAsDomain()
        val mappedMedia = mapMediaAsDomain.map(from = getMediaData())
        assertThat(mappedMedia).isEqualTo(getMedia())
    }

    @Test
    fun `mapping MediaDomain to MediaData returns correct data`() {
        val mapMediaAsDomain = MapMediaAsData()
        val mappedMedia = mapMediaAsDomain.map(from = getMedia())
        assertThat(mappedMedia).isEqualTo(getMediaData())
    }

    private fun getMedia() =
        Media(
            mediaId = 1,
            uriString = "path",
            displayName = "Hosein Sadon",
            mimeType = "image/*",
            duration = "60",
            dateAdded = timeInMillis,
            dateModified = timeInMillis,
            size = 1024,
            bucketDisplayName = "sampleBucketDisplayName",
        )

    private fun getMediaData() =
        MediaData(
            mediaId = 1,
            uriString = "path",
            displayName = "Hosein Sadon",
            mimeType = "image/*",
            duration = "60",
            dateAdded = timeInMillis,
            dateModified = timeInMillis,
            size = 1024,
            bucketDisplayName = "sampleBucketDisplayName",
        )
}
