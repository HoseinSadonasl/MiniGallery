package com.hotaku.media.screens.media_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotaku.media.mapper.MapMediaUiAsMedia
import com.hotaku.media.model.MediaUi
import com.hotaku.media_domain.usecase.DeleteMediaUseCase
import com.hotaku.media_domain.usecase.UpdateMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MediaDetailViewModel
    @Inject
    constructor(
        private val deleteMediaUseCase: DeleteMediaUseCase,
        private val updateMediaUseCase: UpdateMediaUseCase,
        private val mapMediaUiAsMedia: MapMediaUiAsMedia,
    ) : ViewModel() {
        private var mediaDetailViewModlState = MutableStateFlow(MediaDetailUiState())
        val mediaDetailUiState: StateFlow<MediaDetailUiState> = mediaDetailViewModlState.asStateFlow()

        private var mediaDetailViewModelEvents = Channel<MediaDetailScreenEvents>()
        val mediaDetailUiEvents = mediaDetailViewModelEvents.consumeAsFlow()

        fun onAction(action: MediaDetailScreenActions) {
            when (action) {
                is MediaDetailScreenActions.OnAddmediaList -> setMediaList(media = action.media, initialIndex = action.initialIndex)
                is MediaDetailScreenActions.OnNameChange -> setName(newName = action.newName)
                is MediaDetailScreenActions.OnPageChanged -> setSelectedIndex(action.page)
                MediaDetailScreenActions.OnViewMedia -> viewMedia()
                MediaDetailScreenActions.OnShareMedia -> shareMedia()
                MediaDetailScreenActions.OnCloseDialog -> closeDialog()
                MediaDetailScreenActions.OnDeleteMedia -> deleteMedia()
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

        private fun closeDialog() {
            mediaDetailViewModlState.update {
                it.copy(
                    mediaDetailDialog = null,
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
                val mediaToUpdate = mapMediaUiAsMedia.map(currentMedia())
                updateMediaUseCase.invoke(media = mediaToUpdate)
            }
        }

        private fun deleteMedia() {
            viewModelScope.launch {
                val mediaUriToDelete = currentMedia()
                mediaUriToDelete.let { mapMediaUiAsMedia.map(it) }.also { media ->
                    deleteMediaUseCase.invoke(media = listOf(media))
                }
                mediaDetailViewModlState.update {
                    it.copy(
                        media = it.media.filterNot { mediaUi -> mediaUi == mediaUriToDelete },
                    )
                }
            }
        }

        private fun setName(newName: String) {
            mediaDetailViewModlState.update {
                it.copy(
                    mediaName = newName,
                )
            }
        }

        private fun setMediaList(
            media: List<MediaUi>,
            initialIndex: Int,
        ) {
            mediaDetailViewModlState.update {
                it.copy(
                    media = media,
                    selectedMediaItemIndex = initialIndex,
                )
            }
        }

        private fun currentMedia(): MediaUi {
            val currentIndex = mediaDetailViewModlState.value.selectedMediaItemIndex
            return mediaDetailViewModlState.value.media[currentIndex]
        }

        private fun sendEvent(event: MediaDetailScreenEvents) {
            viewModelScope.launch {
                mediaDetailViewModelEvents.send(event)
            }
        }
    }
