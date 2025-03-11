package com.hotaku.media_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.hotaku.media_details.navigation.MediaDetailRoute
import com.hotaku.media_domain.usecase.DeleteMediaUseCase
import com.hotaku.media_domain.usecase.GetMediaUseCase
import com.hotaku.media_domain.usecase.RenameMediaUseCase
import com.hotaku.ui.MediaDialogs
import com.hotaku.ui.mappers.MapMediaAsMediaUi
import com.hotaku.ui.mappers.MapMediaUiAsMedia
import com.hotaku.ui.models.MediaUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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
        private val deleteMediaUseCase: DeleteMediaUseCase,
        private val mapMediaUiAsMedia: MapMediaUiAsMedia,
        private val savedState: SavedStateHandle,
    ) : ViewModel() {
        private var mediaDetailViewModlState = MutableStateFlow(MediaDetailUiState())
        val mediaDetailUiState: StateFlow<MediaDetailUiState> = mediaDetailViewModlState.asStateFlow()

        private var mediaViewModelState = MutableStateFlow<PagingData<MediaUi>>(PagingData.empty())
        val mediaUiState =
            mediaViewModelState
                .onStart { updateMediaState() }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = PagingData.empty(),
                )

        private var mediaDetailViewModelEvents = Channel<MediaDetailScreenEvents>()
        val mediaDetailUiEvents = mediaDetailViewModelEvents.receiveAsFlow()

        init {
            getInitialDataFromSavedState()
        }

        fun onAction(action: MediaDetailScreenActions) {
            when (action) {
                is MediaDetailScreenActions.OnSelectedIndexChanged -> setSelectedIndex(action.index)
                MediaDetailScreenActions.OnPlayVideo -> playVideo()
                MediaDetailScreenActions.OnShareMedia -> shareMedia()
                is MediaDetailScreenActions.OnDeleteMedia -> deleteMedia(media = action.mediaItem)
                is MediaDetailScreenActions.OnMediaNameChange -> setMediaName(mediaName = action.mediaName)
                MediaDetailScreenActions.OnShowOptions -> showOptions()
                MediaDetailScreenActions.OnHideOptions -> showOptions(show = false)
                MediaDetailScreenActions.OnShowOptionsMenu -> showOptionsMenu()
                MediaDetailScreenActions.OnHideOptionsMenu -> showOptionsMenu(show = false)
                MediaDetailScreenActions.OnShowRenameMediaDialog -> openRenameDialog(mediaDialogs = MediaDialogs.RenameMediaDialog)
                MediaDetailScreenActions.OnHideDialog -> openRenameDialog(mediaDialogs = MediaDialogs.Idle)
                MediaDetailScreenActions.OnShowDetails -> {}
                is MediaDetailScreenActions.OnMediaNameQueryChange -> setNameQuery(query = action.query)
                MediaDetailScreenActions.OnClearMediaNameQuery -> setNameQuery(query = "")
                is MediaDetailScreenActions.OnRenameMediaItem -> renameLocalMediaItem(media = action.media)
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
                sendEvent(MediaDetailScreenEvents.OnRefreshMedia)
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
                    .collectLatest { media ->
                        mediaViewModelState.value = media
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
            sendEvent(MediaDetailScreenEvents.OnShareMedia)
        }

        private fun openRenameDialog(mediaDialogs: MediaDialogs) {
            mediaDetailViewModlState.update {
                it.copy(
                    isOptionsMenuVisible = false,
                    dialog = mediaDialogs,
                )
            }
        }

        private fun deleteMedia(media: MediaUi) {
            val mediaUriToDelete = mapMediaUiAsMedia.map(media)
            viewModelScope.launch {
                deleteMediaUseCase.invoke(media = listOf(mediaUriToDelete))
            }
            sendEvent(MediaDetailScreenEvents.OnRefreshMedia)
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
