package com.hotaku.home.mapper

import com.google.common.truth.Truth.assertThat
import com.hotaku.home.model.MediaUi
import com.hotaku.home.utils.MediaType
import com.hotaku.media_domain.model.Media
import org.junit.Test
import java.time.Instant

class MapMediaToMediaUiTest {
    private val mapMediaToMediaUi = MapMediaToMediaUi()
    private var now = Instant.now()

    @Test
    fun mapMediaToMediaUi_returnsCorrectData() {
        val exceptedId: Long = 100
        val exceptedUriString = "content://sample.path"
        val exceptedTime = now.toEpochMilli()
        val domainMediaModel =
            Media(
                mediaId = exceptedId,
                uriString = exceptedUriString,
                displayName = "Hosein Sadon",
                mimeType = "image/*",
                duration = "60",
                dateAdded = exceptedTime,
                dateModified = exceptedTime,
                size = 1024,
                bucketDisplayName = "sampleBucketDisplayName",
            )
        val expectedModel =
            MediaUi(
                mediaId = exceptedId,
                uriString = exceptedUriString,
                displayName = "Hosein Sadon",
                mimeType = MediaType.IMAGE,
                duration = "60",
                dateAdded = Instant.ofEpochMilli(exceptedTime),
                dateModified = Instant.ofEpochMilli(exceptedTime),
                size = 1024,
                bucketDisplayName = "sampleBucketDisplayName",
            )

        val actualMedia = mapMediaToMediaUi.map(domainMediaModel)
        assertThat(actualMedia).isEqualTo(
            expectedModel,
        )
    }
}
