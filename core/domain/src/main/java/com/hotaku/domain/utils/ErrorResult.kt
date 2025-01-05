package com.hotaku.domain.utils

sealed class ErrorResult : Error {
    data object UnknownError : ErrorResult()

    data class LocalError(
        val message: String,
    ) : ErrorResult()

    data class ApiError(
        val code: Int = -1,
        val message: String,
    ) : ErrorResult()
}
