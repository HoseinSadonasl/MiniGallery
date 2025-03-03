package com.hotaku.media.screens.media_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.hotaku.media_domain.usecase.DeleteMediaUseCase
import com.hotaku.media_domain.usecase.SyncMediaUseCase
import com.hotaku.media_domain.util.SyncDataState
import com.hotaku.ui.UiState
import com.hotaku.ui.asUiError
import com.hotaku.ui.mappers.MapMediaUiAsMedia
import com.hotaku.ui.models.MediaUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MediaListViewModel
    @Inject
    constructor(
        private val syncMediaUseCase: SyncMediaUseCase,
        private val deleteMediaUseCase: DeleteMediaUseCase,
        private val mapMediaUiAsMedia: MapMediaUiAsMedia,
    ) : ViewModel() {
        private var mediaListScreenViewModelState = MutableStateFlow(MediaListUiState())
        val mediaListScreenUiState: StateFlow<MediaListUiState> = mediaListScreenViewModelState

        private var synchronizeViewModelState = MutableStateFlow<UiState<Int>?>(null)
        val synchronizeUiState =
            synchronizeViewModelState
                .onStart { synchronizeMedia() }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = null,
                )

        private var mediaViewModelState = MutableStateFlow<PagingData<MediaUi>>(PagingData.empty())
        val mediaUiState = mediaViewModelState.asStateFlow()

        private var viewModelEvents = Channel<MediaListScreenEvents>()
        val mediaScreenEvent = viewModelEvents.receiveAsFlow()

        fun onAction(action: MediaListScreenActions) {
            when (action) {
                MediaListScreenActions.OnRetrySynchronizeMedia -> retrySync()
                MediaListScreenActions.OnHideSyncSection -> setyncSectionStateFalse()
                is MediaListScreenActions.OnMimeTypeChange -> setMimeType(action.mimeType)
                is MediaListScreenActions.OnQueryChange -> setQuery(action.query)
                is MediaListScreenActions.OnSetTopBarVisibility -> setTopBarVisibility(action.visible)
                MediaListScreenActions.OnCollepseSearch -> setSearchExpanded(false)
                MediaListScreenActions.OnExpandSearch -> setSearchExpanded(true)
                is MediaListScreenActions.OnMediaListItemClick -> previewMedia(action.mediaItemIndex)
                MediaListScreenActions.OnMediaListItemLongClick -> {}
                MediaListScreenActions.OnClearSelectedMedia -> clearSelectedMedia()
                is MediaListScreenActions.OnDeleteMediaItem -> deleteMediaItem(mediaUi = action.mediaItem)
                MediaListScreenActions.OnOpenMedia -> showMedia()
                MediaListScreenActions.OnShareMedia -> shareMedia()
            }
        }

        private fun retrySync() = synchronizeMedia()

        private fun deleteMediaItem(mediaUi: MediaUi) {
            val media = listOf(mediaUi)
            deleteMedia(media = media)
        }

        private fun deleteMedia(media: List<MediaUi>) {
            viewModelScope.launch {
                media.map { mapMediaUiAsMedia.map(it) }.let { media ->
                    deleteMediaUseCase.invoke(media = media)
                }
                sendEvent(MediaListScreenEvents.OnRefreshList)
            }
        }

        private fun showMedia() {
            sendEvent(MediaListScreenEvents.OnNavigateToMediaDetail)
        }

        private fun shareMedia() {
            sendEvent(MediaListScreenEvents.OnShareMediaList)
        }

        private fun clearSelectedMedia() {
            mediaListScreenViewModelState.update {
                it.copy(
                    selectedMediaIndex = null,
                )
            }
            sendEvent(MediaListScreenEvents.OnCloseMediaListPreview)
        }

        private fun previewMedia(mediaItemIndex: Int) {
            mediaListScreenViewModelState.update {
                it.copy(
                    selectedMediaIndex = mediaItemIndex,
                )
            }
        }

        private fun setSearchExpanded(expand: Boolean) {
            viewModelScope.launch {
                mediaListScreenViewModelState.update {
                    it.copy(isSearchExpanded = expand)
                }
            }
        }

        private fun setTopBarVisibility(visibility: Boolean) {
            viewModelScope.launch {
                mediaListScreenViewModelState.update {
                    it.copy(isTopBarVisible = visibility)
                }
            }
        }

        private fun setyncSectionStateFalse() {
            viewModelScope.launch {
                mediaListScreenViewModelState.update {
                    it.copy(showSyncSection = false)
                }
            }
        }

        private fun synchronizeMedia() {
            viewModelScope.launch {
                syncMediaUseCase.invoke().collect { result ->
                    synchronizeViewModelState.value =
                        when (result) {
                            SyncDataState.Idle -> null
                            SyncDataState.Syncing -> UiState.Loading()
                            is SyncDataState.SyncFailure -> UiState.Failure(error = result.reason.asUiError())
                            is SyncDataState.SyncSuccess -> UiState.Success(data = result.itemsCount)
                        }
                }
            }
        }

        fun setMediaState(mediaState: PagingData<MediaUi>) {
            mediaViewModelState.value = mediaState
        }

        private fun setMimeType(mimeType: String) {
            mediaListScreenViewModelState.update {
                it.copy(mimeType = mimeType)
            }
        }

        private fun setQuery(query: String) {
            mediaListScreenViewModelState.update {
                it.copy(query = query)
            }
        }

        private fun sendEvent(event: MediaListScreenEvents) {
            viewModelScope.launch {
                viewModelEvents.send(event)
            }
        }
    }
