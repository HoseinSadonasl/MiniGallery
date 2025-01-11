package com.hotaku.domain.utils

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ExecuteFlowTest {
    private val dataToTest: suspend (value: DataResult<String, ErrorResult>) -> Flow<DataResult<String, ErrorResult>> =
        mockk()

    @Test
    fun executeFlow_return_flow_with_loading_result() =
        runTest {
            coEvery { dataToTest.invoke(any()) } returns flowOf(DataResult.Loading)

            dataToTest.invoke(DataResult.Loading).executeFlowResult().test {
                assertThat(awaitItem()).isEqualTo(DataResult.Loading)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun executeFlow_return_flow_with_success_result() =
        runTest {
            val successResult = DataResult.Success(data = "Success result")
            coEvery { dataToTest.invoke(any()) } returns flowOf(successResult)

            dataToTest.invoke(successResult).executeFlowResult().test {
                assertThat(awaitItem()).isEqualTo(DataResult.Loading)
                assertThat(awaitItem()).isEqualTo(successResult)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun executeFlow_return_flow_with_error_result() =
        runTest {
            val errorResult = DataResult.Failure(error = ErrorResult.LocalError(message = "An Error"))
            coEvery { dataToTest.invoke(any()) } returns flowOf(errorResult)

            dataToTest.invoke(errorResult).executeFlowResult().test {
                assertThat(awaitItem()).isEqualTo(DataResult.Loading)
                assertThat(awaitItem()).isEqualTo(errorResult)
                cancelAndIgnoreRemainingEvents()
            }
        }
}
