package com.hotaku.media_library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.hotaku.media_domain.usecase.GetMediaUseCase
import com.hotaku.media_library.MediaLibraryScreenActions.*
import com.hotaku.media_library.utils.LibraryItemsEnum
import com.hotaku.ui.mappers.MapMediaAsMediaUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MediaLibraryViewModel
    @Inject
    constructor(
        private val mediaUseCase: GetMediaUseCase,
        private val mapMediaAsMediaUi: MapMediaAsMediaUi,
    ) : ViewModel() {
        private var mediaLibraryViewModelState = MutableStateFlow(MediaLibraryUiState())
        val mediaLibraryScreenUiState: StateFlow<MediaLibraryUiState> =
            mediaLibraryViewModelState.asStateFlow()

        fun onAction(action: MediaLibraryScreenActions) {
            when (action) {
                OnUpdateMediaState -> updateMediaState()
                OnClearMedia -> onClearMedia()
                OnFavoriteFolderClick -> setSelectedFolder(folder = LibraryItemsEnum.FAVORITE_FOLDER)
                OnSecureFolderClick -> setSelectedFolder(folder = LibraryItemsEnum.SECURE_FOLDER)
                OnTrashFolderClick -> setSelectedFolder(folder = LibraryItemsEnum.TRASH_FOLDER)
                OnCloseSelectedFolder -> setSelectedFolder(folder = null)
            }
        }

        private fun onClearMedia() {
            mediaLibraryViewModelState.update {
                it.copy(
                    media = null,
                )
            }
        }

        private fun setSelectedFolder(folder: LibraryItemsEnum?) {
            mediaLibraryViewModelState.update {
                it.copy(
                    selectedFolder = folder,
                )
            }
        }

        private fun updateMediaState() {
            viewModelScope.launch {
                mediaLibraryViewModelState.value.selectedFolder?.let { folder ->
                    mediaUseCase.invoke(
                        mimeType = mediaLibraryViewModelState.value.mimeType,
                        query = mediaLibraryViewModelState.value.query,
                        albumName = mediaLibraryViewModelState.value.albumName,
                        matchTrash = matchTrash,
                        matchFavorite = matchFavorite,
                    )
                        .cachedIn(viewModelScope)
                        .map { pagingData ->
                            pagingData.map {
                                mapMediaAsMediaUi.map(it)
                            }
                        }
                        .let { media ->
                            mediaLibraryViewModelState.update {
                                it.copy(
                                    media = media,
                                )
                            }
                        }
                }
            }
        }

        private val matchTrash
            get() =
                mediaLibraryViewModelState.value.selectedFolder?.let { folder ->
                    folder == LibraryItemsEnum.TRASH_FOLDER
                } ?: false

        private val matchFavorite
            get() =
                mediaLibraryViewModelState.value.selectedFolder?.let { folder ->
                    folder == LibraryItemsEnum.FAVORITE_FOLDER
                } ?: false
    }
