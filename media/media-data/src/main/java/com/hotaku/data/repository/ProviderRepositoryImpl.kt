package com.hotaku.data.repository

import com.hotaku.data.datasource.ProviderDataSource
import com.hotaku.data.datasource.UpdateMediaDbDataSource
import com.hotaku.domain.utils.DataResult
import com.hotaku.domain.utils.Error
import com.hotaku.domain.utils.ErrorResult
import com.hotaku.media_domain.repository.ProviderRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import javax.inject.Inject

internal class ProviderRepositoryImpl
    @Inject
    constructor(
        private val updateMediaDbDataSource: UpdateMediaDbDataSource,
        private val providerDataSource: ProviderDataSource,
    ) : ProviderRepository {
        override suspend fun updateMediaDatabase(): Flow<DataResult<Int, Error>> =
            flow {
                val result = providerDataSource.getMedia()
                when {
                    result.isFailure -> {
                        emit(
                            DataResult.Failure(
                                ErrorResult.LocalError(
                                    message = result.exceptionOrNull()?.localizedMessage.orEmpty(),
                                ),
                            ),
                        )
                    }

                    result.isSuccess -> {
                        result.getOrNull()?.let { media ->
                            updateMediaDbDataSource.saveMedia(media)
                            emit(DataResult.Success(data = media.size))
                        }
                    }
                }
            }.retryWhen { cuse, attempt ->
                return@retryWhen if (attempt < 3) {
                    delay(UPDATE_DATABASE_RETRY_DELAY)
                    true
                } else {
                    false
                }
            }.catch { exception ->
                exception.printStackTrace()
                emit(DataResult.Failure(ErrorResult.LocalError(message = exception.localizedMessage.orEmpty())))
            }

        companion object {
            private const val UPDATE_DATABASE_RETRY_DELAY = 1500L
        }
    }
