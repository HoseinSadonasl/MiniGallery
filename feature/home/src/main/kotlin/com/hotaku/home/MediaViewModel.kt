package com.hotaku.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.hotaku.domain.utils.DataResult
import com.hotaku.domain.utils.Error
import com.hotaku.domain.utils.ErrorResult
import com.hotaku.feature.home.R
import com.hotaku.home.mapper.MapMediaToMediaUi
import com.hotaku.home.model.MediaUi
import com.hotaku.media_domain.usecase.GetMediaUseCase
import com.hotaku.media_domain.usecase.SyncMediaUseCase
import com.hotaku.ui.UiState
import com.hotaku.ui.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal class MediaViewModel
    @Inject
    constructor(
        private val mapMediaToMediaUi: MapMediaToMediaUi,
        syncMediaUseCase: SyncMediaUseCase,
        mediaUseCase: GetMediaUseCase,
    ) : ViewModel() {
        private var homeScreenViewModelState = MutableStateFlow(HomeScreenUiState())
        val homeScreenUiState: StateFlow<HomeScreenUiState> =
            homeScreenViewModelState.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = homeScreenViewModelState.value.copy(isLoading = true),
            )

        private var synchronizeViewModelState: StateFlow<UiState<Int>> =
            syncMediaUseCase.invoke().map { result ->
                when (result) {
                    is DataResult.Loading -> UiState.Loading()
                    is DataResult.Success -> UiState.Success(data = result.data ?: 0)
                    is DataResult.Failure -> UiState.Failure(error = result.error.asUiError())
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState.Loading(),
            )

        private var mediaViewModelState: StateFlow<PagingData<MediaUi>> =
            mediaUseCase.invoke(
                mimeType = homeScreenViewModelState.value.mimeType,
                query = homeScreenViewModelState.value.query,
            ).map { mediaList -> mediaList.map { media -> mapMediaToMediaUi.map(media) } }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = PagingData.empty(),
            )

        val mediaUiState = mediaViewModelState

        fun onAction(action: HomeScreenActions) {
            when (action) {
                is HomeScreenActions.OnMimeTypeChange -> setMimeType(action.mimeType)
                is HomeScreenActions.OnQueryChange -> setQuery(action.query)
            }
        }

        private fun setMimeType(mimeType: String) {
            homeScreenViewModelState.update {
                it.copy(mimeType = mimeType)
            }
        }

        private fun setQuery(query: String) {
            homeScreenViewModelState.update {
                it.copy(query = query)
            }
        }

        private fun Error.asUiError(): UiText =
            when (val error = this as ErrorResult) {
                is ErrorResult.ApiError -> UiText.DynamicString("${error.message}(${error.code})")
                is ErrorResult.LocalError -> UiText.DynamicString(error.message)
                ErrorResult.UnknownError -> UiText.StringResource(R.string.all_unknown_error)
            }
    }
