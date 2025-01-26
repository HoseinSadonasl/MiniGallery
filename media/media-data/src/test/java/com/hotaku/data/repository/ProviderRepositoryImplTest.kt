package com.hotaku.data.repository

import com.google.common.truth.Truth.assertThat
import com.hotaku.media_domain.repository.ProviderRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ProviderRepositoryImplTest {
    private lateinit var providerRepository: ProviderRepository

    @Before
    fun setUp() {
        providerRepository = mockk()
    }

    @Test
    fun updateDatabase_ifSucceed_shouldReturnSuccessWithDataCount() =
        runTest {
            val expectedData = 20
            val expectedResult = Result.success(expectedData)

            coEvery { providerRepository.updateMediaDatabase() } returns expectedResult

            val actualResult = providerRepository.updateMediaDatabase()

            assertThat(actualResult).isEqualTo(expectedResult)
            assertThat(actualResult.isSuccess).isTrue()
            assertThat(actualResult.getOrNull()).isEqualTo(expectedData)
        }

    @Test
    fun updateDatabase_ifFailed_shouldReturnFailureWithError() =
        runTest {
            val expectedError = Throwable("Error")
            val expectedResult = Result.failure<Int>(expectedError)

            coEvery { providerRepository.updateMediaDatabase() } returns expectedResult

            val actualResult = providerRepository.updateMediaDatabase()

            assertThat(actualResult).isEqualTo(expectedResult)
            assertThat(actualResult.isFailure).isTrue()
            assertThat(actualResult.exceptionOrNull()).isEqualTo(expectedError)
        }
}
