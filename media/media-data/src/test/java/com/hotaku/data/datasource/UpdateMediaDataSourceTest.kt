package com.hotaku.data.datasource

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpdateMediaDataSourceTest {
    private val updateDataSource: UpdateMediaDbDataSource = mockk()

    @Test
    fun getMediaUris_return_uriStringList() =
        runTest {
            val exceptedResult: List<String> = fakeUrisList

            coEvery { updateDataSource.getMediaStringUris() } returns exceptedResult

            val actualResult: List<String> = updateDataSource.getMediaStringUris()

            assertThat(actualResult).isEqualTo(actualResult)
        }

    @Test
    fun deleteNoExistMedia_verify_runs() =
        runTest {
            val uriList: List<String> = fakeUrisList

            coEvery { updateDataSource.deleteNoExistMedia(uriList) } just runs

            updateDataSource.deleteNoExistMedia(uriList)

            coVerify(exactly = 1) {
                updateDataSource.deleteNoExistMedia(uriList)
            }
        }
}

private val fakeUrisList
    get() =
        listOf(
            "content://sampleUri1",
            "content://sampleUri2",
            "content://sampleUri3",
        )
