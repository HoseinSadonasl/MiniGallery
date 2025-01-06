package com.hotaku.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
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
                        millis = now.toString(),
                    ),
                )

            dao.insertAll(media = mediaList)

            val savedMediaList = dao.getAll()

            Truth.assertThat(savedMediaList).isEqualTo(mediaList)
        }
}

private fun fakeMediaEntity(millis: String): MediaEntity =
    MediaEntity(
        mediaId = "id",
        path = "sample/path",
        displayName = "media",
        mimeType = "image",
        dateAdded = millis,
        dateModified = millis,
        size = "1024",
    )
