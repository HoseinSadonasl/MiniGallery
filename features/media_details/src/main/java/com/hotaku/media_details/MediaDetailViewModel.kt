package com.hotaku.media_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import androidx.paging.map
import com.hotaku.media_details.MediaDetailScreenActions.*
import com.hotaku.media_domain.usecase.GetMediaUseCase
import com.hotaku.media_domain.usecase.RenameMediaUseCase
import com.hotaku.media_domain.usecase.TrashMediaUseCase
import com.hotaku.ui.MediaDialogs
import com.hotaku.ui.mappers.MapMediaAsMediaUi
import com.hotaku.ui.mappers.MapMediaUiAsMedia
import com.hotaku.ui.models.MediaUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("complexity.TooManyFunctions")
@HiltViewModel
internal class MediaDetailViewModel
    @Inject
    constructor(
        private val mediaUseCase: GetMediaUseCase,
        private val renameMediaUseCase: RenameMediaUseCase,
        private val mapMediaAsMediaUi: MapMediaAsMediaUi,
        private val trashMediaUseCase: TrashMediaUseCase,
        private val mapMediaUiAsMedia: MapMediaUiAsMedia,
        private val savedState: SavedStateHandle,
    ) : ViewModel() {
        private var mediaDetailViewModlState = MutableStateFlow(MediaDetailUiState())
        val mediaDetailUiState: StateFlow<MediaDetailUiState> =
            mediaDetailViewModlState
                .onStart { updateMediaState() }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = MediaDetailUiState(),
                )

        private var mediaDetailViewModelEvents = Channel<MediaDetailScreenEvents>()
        val mediaDetailUiEvents = mediaDetailViewModelEvents.receiveAsFlow()

        init {
            getInitialDataFromSavedState()
        }

        fun onAction(action: MediaDetailScreenActions) {
            when (action) {
                is OnSelectedIndexChanged -> setSelectedIndex(action.index)
                OnPlayVideo -> playVideo()
                OnShareMedia -> shareMedia()
                is OnTrashMedia -> trashMedia(media = action.mediaItem)
                is OnMediaNameChange -> setMediaName(mediaName = action.mediaName)
                OnShowOptions -> showOptions()
                OnHideOptions -> showOptions(show = false)
                OnShowOptionsMenu -> showOptionsMenu()
                OnHideOptionsMenu -> showOptionsMenu(show = false)
                OnShowRenameMediaDialog -> openRenameDialog(mediaDialogs = MediaDialogs.RenameMediaDialog)
                OnHideDialog -> openRenameDialog(mediaDialogs = MediaDialogs.Idle)
                OnShowDetails -> {}
                is OnMediaNameQueryChange -> setNameQuery(query = action.query)
                OnClearMediaNameQuery -> setNameQuery(query = "")
                is OnRenameMediaItem -> renameLocalMediaItem(media = action.media)
            }
        }

        private fun setMediaName(mediaName: String) {
            mediaDetailViewModlState.update {
                it.copy(
                    mediaName = mediaName,
                )
            }
        }

        private fun renameLocalMediaItem(media: MediaUi) {
            mediaDetailUiState.value.mediaNameQuery.isNotBlank().let { newName ->
                val media = media.copy(displayName = mediaDetailUiState.value.mediaNameQuery)
                viewModelScope.launch {
                    renameMediaUseCase.invoke(
                        media = mapMediaUiAsMedia.map(media),
                    )
                }
                sendEvent(event = MediaDetailScreenEvents.OnRefreshMedia)
            }
        }

        private fun showOptions(show: Boolean = true) {
            mediaDetailViewModlState.update {
                it.copy(
                    isOptionsVisible = show,
                )
            }
        }

        private fun showOptionsMenu(show: Boolean = true) {
            mediaDetailViewModlState.update {
                it.copy(
                    isOptionsMenuVisible = show,
                )
            }
        }

        private fun playVideo() {
            sendEvent(MediaDetailScreenEvents.OnPlayVideo)
        }

        private fun getInitialDataFromSavedState() {
            savedState.toRoute<MediaDetailRoute>().let { initialState ->
                mediaDetailViewModlState.update {
                    it.copy(
                        selectedMediaIndex = initialState.initialItemIndex ?: 0,
                        selectedAlbumName = initialState.selectedAlbum.orEmpty(),
                    )
                }
            }
        }

        private fun updateMediaState() {
            viewModelScope.launch {
                mediaUseCase.invoke(
                    mimeType = "",
                    query = "",
                    albumName = mediaDetailUiState.value.selectedAlbumName,
                )
                    .cachedIn(viewModelScope)
                    .map { pagingData ->
                        pagingData.map {
                            mapMediaAsMediaUi.map(it)
                        }
                    }
                    .let { media ->
                        mediaDetailViewModlState.update {
                            it.copy(
                                media = media,
                            )
                        }
                    }
            }
        }

        private fun setSelectedIndex(page: Int) {
            mediaDetailViewModlState.update {
                it.copy(
                    selectedMediaIndex = page,
                )
            }
        }

        private fun shareMedia() {
            sendEvent(event = MediaDetailScreenEvents.OnShareMedia)
        }

        private fun openRenameDialog(mediaDialogs: MediaDialogs) {
            mediaDetailViewModlState.update {
                it.copy(
                    isOptionsMenuVisible = false,
                    dialog = mediaDialogs,
                )
            }
        }

        private fun trashMedia(media: MediaUi) {
            val mediaUriToDelete = mapMediaUiAsMedia.map(media).copy(isTrash = true)
            viewModelScope.launch {
                trashMediaUseCase.invoke(media = listOf(mediaUriToDelete)).let { isSuccess ->
                    if (isSuccess) {
                        sendEvent(MediaDetailScreenEvents.OnRefreshMedia)
                    }
                }
            }
        }

        private fun setNameQuery(query: String) {
            mediaDetailViewModlState.update {
                it.copy(
                    mediaNameQuery = query,
                )
            }
        }

        private fun sendEvent(event: MediaDetailScreenEvents) {
            viewModelScope.launch {
                mediaDetailViewModelEvents.send(event)
            }
        }
    }
