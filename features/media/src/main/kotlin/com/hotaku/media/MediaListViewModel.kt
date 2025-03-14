package com.hotaku.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.hotaku.media.MediaListScreenActions.*
import com.hotaku.media_domain.usecase.GetMediaUseCase
import com.hotaku.media_domain.usecase.RenameMediaUseCase
import com.hotaku.media_domain.usecase.SyncMediaUseCase
import com.hotaku.media_domain.usecase.TrashMediaUseCase
import com.hotaku.media_domain.util.SyncDataState.*
import com.hotaku.ui.MediaDialogs
import com.hotaku.ui.UiState
import com.hotaku.ui.asUiError
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
internal class MediaListViewModel
    @Inject
    constructor(
        private val syncMediaUseCase: SyncMediaUseCase,
        private val mediaUseCase: GetMediaUseCase,
        private val renameMediaUseCase: RenameMediaUseCase,
        private val mapMediaAsMediaUi: MapMediaAsMediaUi,
        private val trashMediaUseCase: TrashMediaUseCase,
        private val mapMediaUiAsMedia: MapMediaUiAsMedia,
    ) : ViewModel() {
        private var mediaListScreenViewModelState = MutableStateFlow(MediaListUiState())
        val mediaListScreenUiState: StateFlow<MediaListUiState> = mediaListScreenViewModelState.asStateFlow()

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
        val mediaUiState =
            mediaViewModelState
                .onStart { updateMediaState() }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = PagingData.empty(),
                )

        private var viewModelEvents = Channel<MediaListScreenEvents>()
        val mediaScreenEvent = viewModelEvents.receiveAsFlow()

        @Suppress("complexity.LongMethod")
        fun onAction(action: MediaListScreenActions) {
            when (action) {
                OnUpdateUpdateMedia -> updateMediaState()
                is OnSearchQueryChange -> setQuery(query = action.query)
                is OnMimeTypeChange -> setMimeType(mimeType = action.mimeType)
                is OnSearchFocusChanged -> setSearchFocus(hasFocus = action.hasFocus)
                OnRetrySynchronizeMedia -> retrySync()
                OnHideSyncSection -> setyncSectionStateFalse()
                is OnSetTopBarVisibility -> setTopBarVisibility(visibility = action.visible)
                is OnMediaListItemClick -> previewMedia(mediaItemIndex = action.mediaItemIndex)
                is OnSelectedMediaNameChange -> setSelectedMediaName(mediaName = action.mediaName)
                OnMediaListItemLongClick -> {}
                OnClearSelectedMedia -> clearSelectedMedia()
                OnOpenMediaDetails -> showOpenDetails()
                OnPlayVideo -> playVideo()
                OnShareMedia -> shareMedia()
                is OnTrashMediaItem -> trashMediaItem(mediaUi = action.mediaItem)
                OnShowOptions -> showOptions()
                OnHideOptions -> showOptions(show = false)
                OnShowOptionsMenu -> showOptionsMenu()
                OnHideOptionsMenu -> showOptionsMenu(show = false)
                OnHideDiaDialog -> showRenameDialog(mediaDialog = MediaDialogs.Idle)
                OnOpenRenameMediaDialog -> showRenameDialog(mediaDialog = MediaDialogs.RenameMediaDialog)
                is OnMediaNameQueryChange -> setMediaNameQuery(query = action.query)
                OnMediaNameClearQuery -> setMediaNameQuery(query = "")
                is OnRenameMediaItem -> renameLocalMediaItem(media = action.media)
                ShowDetails -> showDetails()
            }
        }

        private fun setSearchFocus(hasFocus: Boolean) {
            mediaListScreenViewModelState.update {
                it.copy(
                    isSearchFocused = hasFocus,
                )
            }
        }

        private fun setSelectedMediaName(mediaName: String) {
            mediaListScreenViewModelState.update {
                it.copy(
                    selectedItemName = mediaName,
                )
            }
        }

        private fun setMediaNameQuery(query: String) {
            mediaListScreenViewModelState.update {
                it.copy(
                    mediaNameQuery = query,
                )
            }
        }

        private fun renameLocalMediaItem(media: MediaUi) {
            mediaListScreenViewModelState.value.mediaNameQuery.isNotBlank().let { newName ->
                val media = media.copy(displayName = mediaListScreenViewModelState.value.mediaNameQuery)
                viewModelScope.launch {
                    renameMediaUseCase.invoke(
                        media = mapMediaUiAsMedia.map(media),
                    ).let { success ->
                        if (success) {
                            sendEvent(event = MediaListScreenEvents.OnRefreshList)
                        }
                    }
                }
            }
        }

        private fun showOptions(show: Boolean = true) {
            mediaListScreenViewModelState.update {
                it.copy(
                    isOptionsVisible = show,
                )
            }
        }

        private fun showDetails() {
        }

        private fun showRenameDialog(mediaDialog: MediaDialogs) {
            mediaListScreenViewModelState.update {
                it.copy(
                    dialog = mediaDialog,
                    isOptionsMenuVisible = false,
                )
            }
        }

        private fun showOptionsMenu(show: Boolean = true) {
            mediaListScreenViewModelState.update {
                it.copy(isOptionsMenuVisible = show)
            }
        }

        private fun playVideo() {
            sendEvent(event = MediaListScreenEvents.OnPlayVideo)
        }

        private fun updateMediaState() {
            viewModelScope.launch {
                mediaUseCase.invoke(
                    mimeType = mediaListScreenUiState.value.mimeType,
                    query = mediaListScreenUiState.value.query,
                    albumName = mediaListScreenUiState.value.albumName,
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

        private fun retrySync() = synchronizeMedia()

        private fun trashMediaItem(mediaUi: MediaUi) {
            val media = listOf(mediaUi)
            trashMedia(media = media)
        }

        private fun trashMedia(media: List<MediaUi>) {
            viewModelScope.launch {
                media.map {
                    mapMediaUiAsMedia.map(it).copy(isTrash = true)
                }.let { media ->
                    trashMediaUseCase.invoke(media = media)
                }.let { success ->
                    if (success) sendEvent(event = MediaListScreenEvents.OnRefreshList)
                }
            }
        }

        private fun showOpenDetails() {
            sendEvent(event = MediaListScreenEvents.OnNavigateToMediaDetail)
        }

        private fun shareMedia() {
            sendEvent(event = MediaListScreenEvents.OnShareMediaList)
        }

        private fun clearSelectedMedia() {
            mediaListScreenViewModelState.update {
                it.copy(
                    selectedMediaIndex = null,
                )
            }
            sendEvent(event = MediaListScreenEvents.OnCloseMediaListPreview)
        }

        private fun previewMedia(mediaItemIndex: Int) {
            mediaListScreenViewModelState.update {
                it.copy(
                    selectedMediaIndex = mediaItemIndex,
                )
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
                            Idle -> null
                            Syncing -> UiState.Loading()
                            is SyncFailure -> UiState.Failure(error = result.reason.asUiError())
                            is SyncSuccess -> UiState.Success(data = result.itemsCount)
                        }
                }
            }
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
