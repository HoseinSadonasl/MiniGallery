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
            val eceptedData = 20
            coEvery { providerRepository.updateMediaDatabase() } returns
                flowOf(
                    DataResult.Success(
                        eceptedData,
                    ),
                )
            providerRepository.updateMediaDatabase().test {
                assertThat(awaitItem()).isEqualTo(DataResult.Success(eceptedData))
                awaitComplete()
            }
        }

    @Test
    fun updateDatabase_ifFailed_shouldEmitDataResultFailureWithMessage() =
        runTest {
            val exceptedError = "An Error"
            coEvery { providerRepository.updateMediaDatabase() } returns
                flowOf(
                    DataResult.Failure(
                        ErrorResult.LocalError(exceptedError),
                    ),
                )
            providerRepository.updateMediaDatabase().test {
                assertThat(awaitItem()).isEqualTo(
                    DataResult.Failure(
                        ErrorResult.LocalError(
                            exceptedError,
                        ),
                    ),
                )
                awaitComplete()
            }
        }

    @Test
    fun updateDatabase_ifFailed_shouldEmitDataResultFailureWithUnknownError() =
        runTest {
            val exceptedErrorResult = ErrorResult.UnknownError
            coEvery { providerRepository.updateMediaDatabase() } returns
                flowOf(
                    DataResult.Failure(
                        exceptedErrorResult,
                    ),
                )
            providerRepository.updateMediaDatabase().test {
                assertThat(awaitItem()).isEqualTo(
                    DataResult.Failure(
                        exceptedErrorResult,
                    ),
                )
                awaitComplete()
            }
        }
}
