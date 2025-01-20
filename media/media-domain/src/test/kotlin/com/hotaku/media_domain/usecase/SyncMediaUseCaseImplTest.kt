package com.hotaku.media_domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.hotaku.domain.utils.DataResult
import com.hotaku.domain.utils.ErrorResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SyncMediaUseCaseImplTest {
    private val syncMediaUseCase: SyncMediaUseCase = mockk()

    @Test
    fun invoke_whenSynchronizeSucceeds_shouldEmitSuccessResult() =
        runTest {
            val expectedResult = 10
            coEvery { syncMediaUseCase.invoke() } returns flowOf(DataResult.Success(data = expectedResult))

            syncMediaUseCase.invoke().test {
                assertThat(awaitItem()).isEqualTo(DataResult.Success(data = expectedResult))
                awaitComplete()
            }
        }

    @Test
    fun invoke_whenSynchronizeFails_shouldEmitLocalErrorResult() =
        runTest {
            val expectedErrorResult = ErrorResult.LocalError.SYNC_DATA_ERROR
            coEvery { syncMediaUseCase.invoke() } returns flowOf(DataResult.Failure(error = expectedErrorResult))

            syncMediaUseCase.invoke().test {
                val result = awaitItem()
                assertThat(result).isInstanceOf(DataResult.Failure::class.java)
                val failureResult = (result as DataResult.Failure).error
                assertThat(failureResult).isEqualTo(expectedErrorResult)
                awaitComplete()
            }
        }
}
