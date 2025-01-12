package com.hotaku.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.hotaku.domain.utils.DataResult
import com.hotaku.domain.utils.ErrorResult
import com.hotaku.media_domain.repository.ProviderRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
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
    fun updateDatabase_atBegining_shouldEmitDataResultLoading() =
        runTest {
            coEvery { providerRepository.updateMediaDatabase() } returns flowOf(DataResult.Loading)
            providerRepository.updateMediaDatabase().test {
                assertThat(awaitItem()).isEqualTo(DataResult.Loading)
                awaitComplete()
            }
        }

    @Test
    fun updateDatabase_ifSucceed_shouldEmitDataResultSuccessWithDataCount() =
        runTest {
            coEvery { providerRepository.updateMediaDatabase() } returns flowOf(DataResult.Success(1))
            providerRepository.updateMediaDatabase().test {
                assertThat(awaitItem()).isEqualTo(DataResult.Success(1))
                awaitComplete()
            }
        }

    @Test
    fun updateDatabase_ifFailed_shouldEmitDataResultFailureWithMessage() =
        runTest {
            coEvery { providerRepository.updateMediaDatabase() } returns
                flowOf(
                    DataResult.Failure(
                        ErrorResult.LocalError("Error"),
                    ),
                )
            providerRepository.updateMediaDatabase().test {
                assertThat(awaitItem())
                    .isEqualTo(DataResult.Failure(ErrorResult.LocalError("Error")))
                awaitComplete()
            }
        }
}
