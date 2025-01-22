package com.hotaku.domain.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import java.io.IOException

fun <T> Flow<DataResult<T, Error>>.executeFlowResult() =
    catch { exception ->
        emit(DataResult.Failure(exception.asException()))
    }.onStart {
        emit(DataResult.Loading)
    }

private fun Throwable.asException(): Error =
    when (this as Exception) {
        is IOException -> ErrorResult.LocalError.IO
        else -> ErrorResult.LocalError.UNKNOWN
    }
