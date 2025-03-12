package com.hotaku.ui.mappers

import com.google.common.truth.Truth.assertThat
import com.hotaku.media_domain.models.Media
import com.hotaku.ui.MediaType
import com.hotaku.ui.models.MediaUi
import org.junit.Test
import java.time.Instant

class MapMediaAsMediaUiTest {
    private val mapMediaAsMediaUi = MapMediaAsMediaUi()
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
                isTrash = false,
                isFavorite = false,
                bucketDisplayName = "sampleBucketDisplayName",
            )
        val expectedModel =
            MediaUi(
                mediaId = exceptedId,
                uriString = exceptedUriString,
                displayName = "Hosein Sadon",
                mimeType = MediaType.IMAGE,
                duration = 60,
                dateAdded = Instant.ofEpochMilli(exceptedTime),
                dateModified = Instant.ofEpochMilli(exceptedTime),
                size = 1024,
                bucketDisplayName = "sampleBucketDisplayName",
            )

        val actualMedia = mapMediaAsMediaUi.map(domainMediaModel)
        assertThat(actualMedia).isEqualTo(
            expectedModel,
        )
    }
}
