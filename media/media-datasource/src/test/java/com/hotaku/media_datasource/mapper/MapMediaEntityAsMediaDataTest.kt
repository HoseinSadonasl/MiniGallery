package com.hotaku.media_datasource.mapper

import com.google.common.truth.Truth
import com.hotaku.data.model.MediaData
import com.hotaku.database.entity.MediaEntity
import org.junit.Before
import org.junit.Test
import java.time.Instant

class MapMediaEntityAsMediaDataTest {
    private var instant: Instant? = null
    private lateinit var mapMediaEntityAsMediaData: MapMediaEntityAsMediaData

    @Before
    fun setUp() {
        mapMediaEntityAsMediaData = MapMediaEntityAsMediaData()
        instant = Instant.now()
    }

    @Test
    fun map_media_entity_as_media_data_return_correct_data() {
        val mediaEntity = fakeMediaEntity(millis = instant!!.toEpochMilli().toString())
        val mappedMedia = mapMediaEntityAsMediaData.map(mediaEntity)
        Truth.assertThat(mappedMedia).isEqualTo(fakeMediaData(instant!!.toEpochMilli()))
    }
}

private fun fakeMediaEntity(millis: String) =
    MediaEntity(
        mediaId = "100",
        path = "sample/path",
        displayName = "mediaName",
        mimeType = "Image",
        dateAdded = millis,
        dateModified = millis,
        size = "1024",
    )

private fun fakeMediaData(millis: Long) =
    MediaData(
        mediaId = 100,
        path = "sample/path",
        displayName = "mediaName",
        mimeType = "Image",
        dateAdded = millis,
        dateModified = millis,
        size = 1024,
    )
