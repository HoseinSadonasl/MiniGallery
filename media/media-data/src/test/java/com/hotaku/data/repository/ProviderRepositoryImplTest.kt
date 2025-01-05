package com.hotaku.data.repository

import com.google.common.truth.Truth
import com.hotaku.domain.utils.DataResult
import com.hotaku.domain.utils.ErrorResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ProviderRepositoryImplTest {
    private lateinit var providerRepository: ProviderRepositoryImpl

    @Before
    fun setUp() {
        providerRepository = mockk()
    }

    @Test
    fun update_database_return_loading() =
        runTest {
            coEvery { providerRepository.updateMediaDatabase() } returns flowOf(DataResult.Loading)
            val updating = providerRepository.updateMediaDatabase().last()
            Truth.assertThat(updating).isEqualTo(DataResult.Loading)
        }

    @Test
    fun update_database_success_and_return_media_count() =
        runTest {
            coEvery { providerRepository.updateMediaDatabase() } returns flowOf(DataResult.Success(1))
            val successUpdate = providerRepository.updateMediaDatabase().last()
            Truth.assertThat(successUpdate).isEqualTo(DataResult.Success(1))
        }

    @Test
    fun update_database_error_and_return_error_messaget() =
        runTest {
            coEvery { providerRepository.updateMediaDatabase() } returns
                flowOf(
                    DataResult.Failure(
                        ErrorResult.LocalError("Error"),
                    ),
                )
            val failureUpdate = providerRepository.updateMediaDatabase().last()
            Truth.assertThat(failureUpdate)
                .isEqualTo(DataResult.Failure(ErrorResult.LocalError("Error")))
        }
}
