package com.hotaku.media.screens.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.hotaku.domain.utils.DataResult
import com.hotaku.media.mapper.MapMediaToMediaUi
import com.hotaku.media.model.AlbumUi
import com.hotaku.media.model.MediaUi
import com.hotaku.media.utils.asUiError
import com.hotaku.media_domain.usecase.GetMediaUseCase
import com.hotaku.media_domain.usecase.SyncMediaUseCase
import com.hotaku.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
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
        private var mediaScreenViewModelState = MutableStateFlow(MediaUiState())
        val mediaScreenUiState: StateFlow<MediaUiState> = mediaScreenViewModelState

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

        private var viewModelEvents = Channel<MediaScreenEvents>()
        val mediaScreenEvent = viewModelEvents.receiveAsFlow()

        fun onAction(action: MediaScreenActions) {
            when (action) {
                MediaScreenActions.OnUpdateMedia -> updateMedia()
                MediaScreenActions.OnHideSyncSection -> setyncSectionStateFalse()
                is MediaScreenActions.OnMimeTypeChange -> setMimeType(action.mimeType)
                is MediaScreenActions.OnQueryChange -> setQuery(action.query)
                is MediaScreenActions.OnSetTopBarVisibility -> setTopBarVisibility(action.visible)
                MediaScreenActions.OnCollepseSearch -> setSearchExpanded(false)
                MediaScreenActions.OnExpandSearch -> setSearchExpanded(true)
                is MediaScreenActions.OnAlbumSelected -> onAlbumSelected(action.album)
                MediaScreenActions.OnClearSelectedAlbum -> clearSelectedAlbum()
                is MediaScreenActions.OnMediaClick -> openMedia(action.mediaUi)
                MediaScreenActions.OnMediaLongClick -> {}
                MediaScreenActions.OnClearSelectedMedia -> clearSelectedMedia()
            }
        }

        private fun clearSelectedMedia() {
            mediaScreenViewModelState.update {
                it.copy(
                    selectedMedia = null,
                )
            }
            sendEvent(MediaScreenEvents.OnCloseMediaPreview)
        }

        private fun openMedia(mediaUi: MediaUi) {
            mediaScreenViewModelState.update {
                it.copy(
                    selectedMedia = mediaUi,
                )
            }
        }

        private fun clearSelectedAlbum() {
            mediaScreenViewModelState.update {
                it.copy(
                    selectedAlbum = null,
                )
            }
        }

        private fun onAlbumSelected(album: AlbumUi) {
            mediaScreenViewModelState.update {
                it.copy(
                    selectedAlbum = album,
                )
            }
        }

        private fun setSearchExpanded(expand: Boolean) {
            viewModelScope.launch {
                mediaScreenViewModelState.update {
                    it.copy(isSearchExpanded = expand)
                }
            }
        }

        private fun setTopBarVisibility(visibility: Boolean) {
            viewModelScope.launch {
                mediaScreenViewModelState.update {
                    it.copy(isTopBarVisible = visibility)
                }
            }
        }

        private fun setyncSectionStateFalse() {
            viewModelScope.launch {
                mediaScreenViewModelState.update {
                    it.copy(showSyncSection = false)
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
                    mimeType = mediaScreenViewModelState.value.mimeType,
                    query = mediaScreenViewModelState.value.query,
                )
                    .cachedIn(viewModelScope)
                    .collect { pagedMedia ->
                        mediaViewModelState.value = pagedMedia.map { mapMediaToMediaUi.map(it) }
                    }
            }
        }

        private fun setMimeType(mimeType: String) {
            mediaScreenViewModelState.update {
                it.copy(mimeType = mimeType)
            }
        }

        private fun setQuery(query: String) {
            mediaScreenViewModelState.update {
                it.copy(query = query)
            }
        }

        private fun sendEvent(event: MediaScreenEvents) {
            viewModelScope.launch {
                viewModelEvents.send(event)
            }
        }
    }
