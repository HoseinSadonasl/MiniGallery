package com.hotaku.domain.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DataResultTest {
    @Test
    fun `should return loading when await for result`() {
        val loadingState = DataResult.Loading
        assertThat(loadingState).isInstanceOf(DataResult.Loading::class.java)
    }

    @Test
    fun `should return data when data result was successful`() {
        val successResult = DataResult.Success(data = "Hello world")
        assertThat(successResult).isInstanceOf(DataResult.Success::class.java)
        assertThat(successResult.data).isEqualTo("Hello world")
    }

    @Test
    fun `should return ApiError when api response return error`() {
        val apiFailure =
            DataResult.Failure(error = ErrorResult.ApiError(code = 404, message = "Not Found"))
        assertThat(apiFailure.error).isInstanceOf(ErrorResult.ApiError::class.java)
        assertThat(apiFailure.error.code).isEqualTo(404)
        assertThat(apiFailure.error.message).isEqualTo("Not Found")
    }

    @Test
    fun `should return LocalError when operation unsuccessful`() {
        val localFailure =
            DataResult.Failure(error = ErrorResult.LocalError.IO)
        assertThat(localFailure.error).isInstanceOf(ErrorResult.LocalError::class.java)
        assertThat(localFailure.error).isEqualTo(ErrorResult.LocalError.IO)
    }
}
