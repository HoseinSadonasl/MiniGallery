package com.hotaku.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.hotaku.database.dao.MediaDao
import com.hotaku.database.entity.MediaEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Instant

@SmallTest
class MediaDatabaseTest {
    private var now: Long? = null
    private lateinit var dao: MediaDao
    private lateinit var database: MiniGalleryDataBase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder(
                context, MiniGalleryDataBase::class.java,
            ).build()
        dao = database.mediaDao()
        now = Instant.now().toEpochMilli()
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun upsert_and_get_all_media_from_database() =
        runTest {
            val mediaList =
                listOf(
                    fakeMediaEntity(
                        mediaId = "100",
                        mediaName = "SampleName1",
                        millis = now.toString(),
                        mimeType = "Image",
                    ),
                    fakeMediaEntity(
                        mediaId = "101",
                        mediaName = "SampleName1",
                        millis = now.toString(),
                        mimeType = "Image",
                    ),
                )

            dao.insertAll(media = mediaList)

            val savedMediaList = dao.getAll(limit = 1, offset = 0)

            Truth.assertThat(savedMediaList).isEqualTo(listOf(mediaList.first()))
        }

    @Test
    fun get_all_media_with_specified_mimeType_from_database() =
        runTest {
            val mediaList =
                listOf(
                    fakeMediaEntity(
                        mediaId = "100",
                        mediaName = "SampleName1",
                        millis = now.toString(),
                        mimeType = "Image",
                    ),
                    fakeMediaEntity(
                        mediaId = "200",
                        mediaName = "SampleName2",
                        millis = now.toString(),
                        mimeType = "Video",
                    ),
                )

            dao.insertAll(media = mediaList)

            val savedMediaList = dao.getAll(mimeType = "Image", limit = 1, offset = 0)

            Truth.assertThat(savedMediaList.first().mimeType).isEqualTo("Image")
        }

    @Test
    fun get_all_media_with_specified_query_from_database() =
        runTest {
            val mediaList =
                listOf(
                    fakeMediaEntity(
                        mediaId = "100",
                        mediaName = "SampleName1",
                        millis = now.toString(),
                        mimeType = "Image",
                    ),
                    fakeMediaEntity(
                        mediaId = "200",
                        mediaName = "SampleName2",
                        millis = now.toString(),
                        mimeType = "Video",
                    ),
                )

            dao.insertAll(media = mediaList)

            val savedMediaList = dao.getAll(query = "SampleName1", limit = 1, offset = 0)

            Truth.assertThat(savedMediaList.first().displayName).isEqualTo("SampleName1")
        }

    @Test
    fun getAllUris_shouldReturnAllExistingUrils() =
        runTest {
            val mediaList =
                listOf(
                    fakeMediaEntity(
                        mediaId = "100",
                        mediaName = "SampleName1",
                        millis = now.toString(),
                        mimeType = "Image",
                    ),
                    fakeMediaEntity(
                        mediaId = "200",
                        mediaName = "SampleName2",
                        millis = now.toString(),
                        mimeType = "Video",
                    ),
                )

            dao.insertAll(media = mediaList)

            val exceptedUris = mediaList.map { it.uriString }

            val existingUris = dao.getAllUris()

            assertThat(existingUris).isEqualTo(exceptedUris)
        }

    @Test
    fun deleteBtUris_deleteMediasByGivenUris() =
        runTest {
            val mediaList =
                listOf(
                    fakeMediaEntity(
                        mediaId = "100",
                        mediaName = "SampleName1",
                        millis = now.toString(),
                        mimeType = "Image",
                    ),
                    fakeMediaEntity(
                        mediaId = "200",
                        mediaName = "SampleName2",
                        millis = now.toString(),
                        mimeType = "Video",
                    ),
                )

            dao.insertAll(media = mediaList)

            val urisToDelete = mediaList.map { it.uriString }

            dao.deleteByUris(urisToDelete)

            val existingUris = dao.getAllUris()

            assertThat(existingUris).isEmpty()
        }
}

private fun fakeMediaEntity(
    mediaId: String = "100",
    mediaName: String,
    millis: String,
    mimeType: String,
) = MediaEntity(
    mediaId = mediaId,
    uriString = "sample/path",
    displayName = mediaName,
    mimeType = mimeType,
    duration = "60",
    dateAdded = millis,
    dateModified = millis,
    size = "1024",
)
