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
    fun `should return UnkownErrorwhen data result broken because of unknown reason`() {
        val errorResult = DataResult.Error(error = ErrorResult.UnknownError)
        assertThat(errorResult.error).isInstanceOf(ErrorResult.UnknownError::class.java)
    }

    @Test
    fun `should return ApiError when api response return error`() {
        val apiError =
            DataResult.Error(error = ErrorResult.ApiError(code = 404, message = "Not Found"))
        assertThat(apiError.error).isInstanceOf(ErrorResult.ApiError::class.java)
        assertThat(apiError.error.code).isEqualTo(404)
        assertThat(apiError.error.message).isEqualTo("Not Found")
    }

    @Test
    fun `should return LocalError when operation unsuccessful`() {
        val localError =
            DataResult.Error(error = ErrorResult.LocalError(message = "Database Error"))
        assertThat(localError.error).isInstanceOf(ErrorResult.LocalError::class.java)
        assertThat(localError.error.message).isEqualTo("Database Error")
    }
}
