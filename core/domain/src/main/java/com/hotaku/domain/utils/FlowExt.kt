package com.hotaku.domain.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import java.io.IOException

fun <T> Flow<DataResult<T, Error>>.executeFlowResult(coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO) =
    catch { exception ->
        emit(DataResult.Failure(exception.asException()))
    }.onStart {
        emit(DataResult.Loading)
    }.flowOn(coroutineDispatcher)

private fun Throwable.asException(): Error =
    when (val exception = this as Exception) {
        is IOException -> ErrorResult.LocalError(exception.localizedMessage)
        else -> ErrorResult.UnknownError
    }
