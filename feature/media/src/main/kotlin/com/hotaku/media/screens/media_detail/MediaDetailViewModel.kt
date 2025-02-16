package com.hotaku.media.screens.media_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotaku.media.mapper.MapMediaUiAsMedia
import com.hotaku.media.model.MediaUi
import com.hotaku.media.screens.media_list.MediaViewModel
import com.hotaku.media_domain.usecase.DeleteMediaUseCase
import com.hotaku.media_domain.usecase.UpdateMediaUseCase
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
internal class MediaDetailViewModel
    @Inject
    constructor(
        private val deleteMediaUseCase: DeleteMediaUseCase,
        private val updateMediaUseCase: UpdateMediaUseCase,
        private val mapMediaUiAsMedia: MapMediaUiAsMedia,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private var mediaDetailViewModlState = MutableStateFlow(MediaDetailUiState())
        val mediaDetailUiState: StateFlow<MediaDetailUiState> =
            mediaDetailViewModlState
                .onStart {
                    getSelectedIndex()
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = MediaDetailUiState(),
                )

        fun onAction(action: MediaDetailScreenActions) {
            when (action) {
                is MediaDetailScreenActions.OnAddmediaList -> setMediaList(media = action.media)
                is MediaDetailScreenActions.OnNameChange -> setName(newName = action.newName)
                MediaDetailScreenActions.OnDeleteMedia -> deleteMedia()
                MediaDetailScreenActions.OnOOpenMenu -> openMenuPopup()
                MediaDetailScreenActions.OnCloseMenu -> openMenuPopup(open = false)
                MediaDetailScreenActions.OnRenameClick -> openRenameDialog()
                MediaDetailScreenActions.OnSubmitRenameClick -> openRenameDialog(open = false)
                MediaDetailScreenActions.OnUpdateMedia -> updateMedia()
            }
        }

        private fun getSelectedIndex() {
            mediaDetailViewModlState.update {
                it.copy(
                    selectedMediaItemIndex = savedStateHandle[MediaViewModel.SELECTED_MEDIA_INDEX] ?: 0,
                )
            }
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
                val mediaUriToDelete = currentMedia().uriString
                deleteMediaUseCase.invoke(mediaUriString = mediaUriToDelete)
            }
        }

        private fun setName(newName: String) {
            mediaDetailViewModlState.update {
                it.copy(
                    mediaName = newName,
                )
            }
        }

        private fun setMediaList(media: List<MediaUi>) {
            mediaDetailViewModlState.update {
                it.copy(
                    media = media,
                )
            }
        }

        private fun currentMedia(): MediaUi {
            val currentIndex = mediaDetailViewModlState.value.selectedMediaItemIndex
            return mediaDetailViewModlState.value.media[currentIndex]
        }
    }
