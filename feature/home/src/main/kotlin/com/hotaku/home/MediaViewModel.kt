package com.hotaku.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MediaViewModel
    @Inject
    constructor(
        private val mapMediaToMediaUi: MapMediaToMediaUi,
        private val syncMediaUseCase: SyncMediaUseCase,
        private val mediaUseCase: GetMediaUseCase,
    ) : ViewModel() {
        private var homeScreenViewModelState = MutableStateFlow(HomeScreenUiState())
        val homeScreenUiState: StateFlow<HomeScreenUiState> = homeScreenViewModelState

        private var synchronizeViewModelState = MutableStateFlow<UiState<Int>>(UiState.Loading())
        val synchronizeUiState =
            synchronizeViewModelState
                .onStart { synchronizeMedia() }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = UiState.Loading(),
                )

        private var mediaViewModelState = MutableStateFlow<PagingData<MediaUi>>(PagingData.empty())
        val mediaUiState =
            mediaViewModelState
                .onStart { updateMedia() }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = PagingData.empty(),
                )

        fun onAction(action: HomeScreenActions) {
            when (action) {
                HomeScreenActions.OnUpdateMedia -> updateMedia()
                HomeScreenActions.OnHideSyncSection -> setyncSectionStateFalse()
                is HomeScreenActions.OnMimeTypeChange -> setMimeType(action.mimeType)
                is HomeScreenActions.OnQueryChange -> setQuery(action.query)
                is HomeScreenActions.OnScrolled -> setScrollState(action.isScrolled)
            }
        }

        private fun setScrollState(scrolled: Boolean) {
            viewModelScope.launch {
                homeScreenViewModelState.update {
                    it.copy(isScrolled = scrolled)
                }
            }
        }

        private fun setyncSectionStateFalse() {
            viewModelScope.launch {
                homeScreenViewModelState.update {
                    it.copy(shoSyncSection = false)
                }
            }
        }

        private fun synchronizeMedia() {
            viewModelScope.launch {
                syncMediaUseCase.invoke().collect { result ->
                    synchronizeViewModelState.value =
                        when (result) {
                            is DataResult.Loading -> UiState.Loading()
                            is DataResult.Success -> UiState.Success(data = result.data ?: 0)
                            is DataResult.Failure -> UiState.Failure(error = result.error.asUiError())
                        }
                }
            }
        }

        private fun updateMedia() {
            viewModelScope.launch {
                mediaUseCase.invoke(
                    mimeType = homeScreenViewModelState.value.mimeType,
                    query = homeScreenViewModelState.value.query,
                )
                    .cachedIn(viewModelScope)
                    .collect { pagedMedia ->
                        mediaViewModelState.value = pagedMedia.map { mapMediaToMediaUi.map(it) }
                    }
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
                is ErrorResult.LocalError -> {
                    when (error) {
                        ErrorResult.LocalError.UNKNOWN -> UiText.StringResource(R.string.all_unknown_error)
                        ErrorResult.LocalError.DISK_FULL -> UiText.StringResource(R.string.all_disk_full)
                        ErrorResult.LocalError.IO -> UiText.StringResource(R.string.all_io_error)
                        ErrorResult.LocalError.READ_DATA_ERROR -> UiText.StringResource(R.string.all_io_error)
                        ErrorResult.LocalError.SYNC_DATA_ERROR -> UiText.StringResource(R.string.all_sync_error)
                    }
                }
            }
    }
