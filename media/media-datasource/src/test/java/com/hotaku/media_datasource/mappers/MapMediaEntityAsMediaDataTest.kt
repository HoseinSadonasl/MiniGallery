package com.hotaku.media_datasource.mappers

import com.google.common.truth.Truth
import com.hotaku.data.modes.MediaData
import com.hotaku.media_datasource.entities.MediaEntity
import org.junit.Before
import org.junit.Test
import java.time.Instant

internal class MapMediaEntityAsMediaDataTest {
    private var instant: Instant? = null
    private lateinit var mapMediaEntityAsMediaData: MapMediaEntityAsMediaData

    @Before
    fun setUp() {
        mapMediaEntityAsMediaData = MapMediaEntityAsMediaData()
        instant = Instant.now()
    }

    @Test
    fun mapMediaEntityAsMediaData_shouldReturnCorrectData() {
        val mediaEntity = fakeMediaEntity(millis = instant!!.toEpochMilli().toString())
        val mappedMedia = mapMediaEntityAsMediaData.map(mediaEntity)
        Truth.assertThat(mappedMedia).isEqualTo(fakeMediaData(instant!!.toEpochMilli()))
    }
}

private fun fakeMediaEntity(millis: String) =
    MediaEntity(
        mediaId = "100",
        uriString = "sample/path",
        displayName = "mediaName",
        mimeType = "Image",
        duration = "60",
        dateAdded = millis,
        dateModified = millis,
        size = "1024",
        isTrash = false,
        isFavorite = false,
        bucketDisplayName = "sampleBucketDisplayName",
    )

private fun fakeMediaData(millis: Long) =
    MediaData(
        mediaId = 100,
        uriString = "sample/path",
        displayName = "mediaName",
        mimeType = "Image",
        duration = "60",
        dateAdded = millis,
        dateModified = millis,
        size = 1024,
        isTrash = false,
        isFavorite = false,
        bucketDisplayName = "sampleBucketDisplayName",
    )
