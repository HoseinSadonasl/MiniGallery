package com.hotaku.data.repository

import com.google.common.truth.Truth.assertThat
import com.hotaku.media_domain.repository.UpdateLocalMediaRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateLocalMediaRepositoryImplTest {
    private lateinit var updateLocalMediaRepository: UpdateLocalMediaRepository

    @Before
    fun setUp() {
        updateLocalMediaRepository = mockk()
    }

    @Test
    fun updateDatabase_ifSucceed_shouldReturnSuccessWithDataCount() =
        runTest {
            val expectedData = 20
            val expectedResult = Result.success(expectedData)

            coEvery { updateLocalMediaRepository.update() } returns expectedResult

            val actualResult = updateLocalMediaRepository.update()

            assertThat(actualResult).isEqualTo(expectedResult)
            assertThat(actualResult.isSuccess).isTrue()
            assertThat(actualResult.getOrNull()).isEqualTo(expectedData)
        }

    @Test
    fun updateDatabase_ifFailed_shouldReturnFailureWithError() =
        runTest {
            val expectedError = Throwable("Error")
            val expectedResult = Result.failure<Int>(expectedError)

            coEvery { updateLocalMediaRepository.update() } returns expectedResult

            val actualResult = updateLocalMediaRepository.update()

            assertThat(actualResult).isEqualTo(expectedResult)
            assertThat(actualResult.isFailure).isTrue()
            assertThat(actualResult.exceptionOrNull()).isEqualTo(expectedError)
        }
}
