package com.hotaku.domain.utils

sealed interface DataResult<out D, out E: ErrorResult> {
    data object Loading : DataResult<Nothing, Nothing>
    data class Success<out D>(val data: D?) : DataResult<D, Nothing>
    data class Error<out E: ErrorResult>(val error: E) : DataResult<Nothing, E>
}