package com.hotaku.domain.utils

sealed interface ErrorResult : Error {
    enum class LocalError : ErrorResult {
        UNKNOWN,
        SYNC_DATA_ERROR,
        READ_DATA_ERROR,
        IO,
        DISK_FULL,
    }

    data class ApiError(
        val code: Int = -1,
        val message: String,
    ) : ErrorResult
}
