package com.hotaku.media.screens.media_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.hotaku.media_domain.usecase.DeleteMediaUseCase
import com.hotaku.ui.mappers.MapMediaUiAsMedia
import com.hotaku.ui.models.MediaUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MediaDetailViewModel
    @Inject
    constructor(
        private val deleteMediaUseCase: DeleteMediaUseCase,
        private val mapMediaUiAsMedia: MapMediaUiAsMedia,
    ) : ViewModel() {
        private var mediaDetailViewModlState = MutableStateFlow(MediaDetailUiState())
        val mediaDetailUiState: StateFlow<MediaDetailUiState> = mediaDetailViewModlState.asStateFlow()

        private var mediaViewModelState = MutableStateFlow<PagingData<MediaUi>>(PagingData.empty())
        val mediaUiState = mediaViewModelState.asStateFlow()

        private var mediaDetailViewModelEvents = Channel<MediaDetailScreenEvents>()
        val mediaDetailUiEvents = mediaDetailViewModelEvents.receiveAsFlow()

        fun onAction(action: MediaDetailScreenActions) {
            when (action) {
                is MediaDetailScreenActions.OnNameChange -> setName(newName = action.newName)
                is MediaDetailScreenActions.OnSelectedIndexChanged -> setSelectedIndex(action.index)
                MediaDetailScreenActions.OnViewMedia -> viewMedia()
                MediaDetailScreenActions.OnShareMedia -> shareMedia()
                is MediaDetailScreenActions.OnDeleteMedia -> deleteMedia(media = action.mediaItem)
                MediaDetailScreenActions.OnOOpenMenu -> openMenuPopup()
                MediaDetailScreenActions.OnCloseMenu -> openMenuPopup(open = false)
                MediaDetailScreenActions.OnRenameClick -> openRenameDialog()
                MediaDetailScreenActions.OnSubmitRenameClick -> openRenameDialog(open = false)
                MediaDetailScreenActions.OnUpdateMedia -> updateMedia()
            }
        }

        private fun setSelectedIndex(page: Int) {
            mediaDetailViewModlState.update {
                it.copy(
                    selectedMediaItemIndex = page,
                )
            }
        }

        private fun shareMedia() {
            sendEvent(MediaDetailScreenEvents.OnShareMedia)
        }

        private fun viewMedia() {
            sendEvent(MediaDetailScreenEvents.OnViewMedia)
        }

        private fun openMenuPopup(open: Boolean = true) {
            mediaDetailViewModlState.update {
                it.copy(
                    openMenuPopup = open,
                )
            }
        }

        private fun openRenameDialog(open: Boolean = true) {
            mediaDetailViewModlState.update {
                it.copy(
                    openRenameDialog = open,
                )
            }
        }

        private fun updateMedia() {
            viewModelScope.launch {
                // Update media
            }
        }

        private fun deleteMedia(media: MediaUi) {
            val mediaUriToDelete = mapMediaUiAsMedia.map(media)
            viewModelScope.launch {
                deleteMediaUseCase.invoke(media = listOf(mediaUriToDelete))
            }
            sendEvent(MediaDetailScreenEvents.OnRefreshMedia)
        }

        private fun setName(newName: String) {
            mediaDetailViewModlState.update {
                it.copy(
                    mediaName = newName,
                )
            }
        }

        fun setMediaState(mediaState: PagingData<MediaUi>) {
            mediaViewModelState.value = mediaState
        }

        private fun sendEvent(event: MediaDetailScreenEvents) {
            viewModelScope.launch {
                mediaDetailViewModelEvents.send(event)
            }
        }
    }
