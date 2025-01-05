package com.hotaku.domain.utils

sealed interface DataResult<out D, out E : Error> {
    data object Loading : DataResult<Nothing, Nothing>

    data class Success<out D>(
        val data: D?,
    ) : DataResult<D, Nothing>

    data class Failure<out E : Error>(
        val error: E,
    ) : DataResult<Nothing, E>
}
