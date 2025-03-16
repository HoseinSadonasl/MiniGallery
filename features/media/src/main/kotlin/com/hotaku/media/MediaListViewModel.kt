package com.hotaku.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.hotaku.media.MediaListScreenActions.OnClearSelectedMedia
import com.hotaku.media.MediaListScreenActions.OnHideDiaDialog
import com.hotaku.media.MediaListScreenActions.OnHideOptions
import com.hotaku.media.MediaListScreenActions.OnHideOptionsMenu
import com.hotaku.media.MediaListScreenActions.OnHideSyncSection
import com.hotaku.media.MediaListScreenActions.OnItemIsFavoriteChange
import com.hotaku.media.MediaListScreenActions.OnMediaListItemClick
import com.hotaku.media.MediaListScreenActions.OnMediaListItemLongClick
import com.hotaku.media.MediaListScreenActions.OnMediaNameClearQuery
import com.hotaku.media.MediaListScreenActions.OnMediaNameQueryChange
import com.hotaku.media.MediaListScreenActions.OnMimeTypeChange
import com.hotaku.media.MediaListScreenActions.OnOpenMediaDetails
import com.hotaku.media.MediaListScreenActions.OnOpenRenameMediaDialog
import com.hotaku.media.MediaListScreenActions.OnPlayVideo
import com.hotaku.media.MediaListScreenActions.OnRenameMediaItem
import com.hotaku.media.MediaListScreenActions.OnRetrySynchronizeMedia
import com.hotaku.media.MediaListScreenActions.OnSearchFocusChanged
import com.hotaku.media.MediaListScreenActions.OnSearchQueryChange
import com.hotaku.media.MediaListScreenActions.OnSelectedMediaNameChange
import com.hotaku.media.MediaListScreenActions.OnSetTopBarVisibility
import com.hotaku.media.MediaListScreenActions.OnShareMedia
import com.hotaku.media.MediaListScreenActions.OnShowOptions
import com.hotaku.media.MediaListScreenActions.OnShowOptionsMenu
import com.hotaku.media.MediaListScreenActions.OnTrashMediaItem
import com.hotaku.media.MediaListScreenActions.OnUpdateUpdateMedia
import com.hotaku.media.MediaListScreenActions.ShowDetails
import com.hotaku.media_domain.usecase.FavoriteMediaUseCase
import com.hotaku.media_domain.usecase.GetMediaUseCase
import com.hotaku.media_domain.usecase.RenameMediaUseCase
import com.hotaku.media_domain.usecase.SyncMediaUseCase
import com.hotaku.media_domain.usecase.TrashMediaUseCase
import com.hotaku.media_domain.util.SyncDataState.Idle
import com.hotaku.media_domain.util.SyncDataState.SyncFailure
import com.hotaku.media_domain.util.SyncDataState.SyncSuccess
import com.hotaku.media_domain.util.SyncDataState.Syncing
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
        private val favoriteMediaUseCase: FavoriteMediaUseCase,
        private val mapMediaAsMediaUi: MapMediaAsMediaUi,
        private val trashMediaUseCase: TrashMediaUseCase,
        private val mapMediaUiAsMedia: MapMediaUiAsMedia,
    ) : ViewModel() {
        private var viewModelState = MutableStateFlow(MediaListUiState())
        val state: StateFlow<MediaListUiState> =
            viewModelState
                .onStart {
                    synchronizeMedia()
                    updateMediaState()
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = MediaListUiState(),
                )

        private var viewModelEvent = Channel<MediaListScreenEvents>()
        val event = viewModelEvent.receiveAsFlow()

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
                is OnItemIsFavoriteChange -> markMediaAsFavorite(mediaUi = action.mediaItem)
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
            viewModelState.update {
                it.copy(
                    isSearchFocused = hasFocus,
                )
            }
        }

        private fun setSelectedMediaName(mediaName: String) {
            viewModelState.update {
                it.copy(
                    selectedItemName = mediaName,
                )
            }
        }

        private fun setMediaNameQuery(query: String) {
            viewModelState.update {
                it.copy(
                    mediaNameQuery = query,
                )
            }
        }

        private fun renameLocalMediaItem(media: MediaUi) {
            viewModelState.value.mediaNameQuery.isNotBlank().let { newName ->
                val media = media.copy(displayName = viewModelState.value.mediaNameQuery)
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
            viewModelState.update {
                it.copy(
                    isOptionsVisible = show,
                )
            }
        }

        private fun showDetails() {
        }

        private fun showRenameDialog(mediaDialog: MediaDialogs) {
            viewModelState.update {
                it.copy(
                    dialog = mediaDialog,
                    isOptionsMenuVisible = false,
                )
            }
        }

        private fun showOptionsMenu(show: Boolean = true) {
            viewModelState.update {
                it.copy(isOptionsMenuVisible = show)
            }
        }

        private fun playVideo() {
            sendEvent(event = MediaListScreenEvents.OnPlayVideo)
        }

        private fun updateMediaState() {
            viewModelScope.launch {
                mediaUseCase.invoke(
                    mimeType = state.value.mimeType,
                    query = state.value.query,
                    albumName = state.value.albumName,
                )
                    .cachedIn(viewModelScope)
                    .map { pagingData ->
                        pagingData.map {
                            mapMediaAsMediaUi.map(it)
                        }
                    }
                    .let { media ->
                        viewModelState.update {
                            it.copy(
                                media = media,
                            )
                        }
                    }
            }
        }

        private fun markMediaAsFavorite(mediaUi: MediaUi) {
            viewModelScope.launch {
                val media =
                    mapMediaUiAsMedia.map(from = mediaUi).copy(
                        isFavorite = !mediaUi.isFavorite,
                    )
                favoriteMediaUseCase.invoke(media = media).let { success ->
                    if (success) {
                    }
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
            viewModelState.update {
                it.copy(
                    selectedMediaIndex = null,
                )
            }
            sendEvent(event = MediaListScreenEvents.OnCloseMediaListPreview)
        }

        private fun previewMedia(mediaItemIndex: Int) {
            viewModelState.update {
                it.copy(
                    selectedMediaIndex = mediaItemIndex,
                )
            }
        }

        private fun setTopBarVisibility(visibility: Boolean) {
            viewModelScope.launch {
                viewModelState.update {
                    it.copy(isTopBarVisible = visibility)
                }
            }
        }

        private fun setyncSectionStateFalse() {
            viewModelScope.launch {
                viewModelState.update {
                    it.copy(showSyncSection = false)
                }
            }
        }

        private fun synchronizeMedia() {
            viewModelScope.launch {
                syncMediaUseCase.invoke().collect { result ->
                    viewModelState.update {
                        it.copy(
                            synchronize =
                                when (result) {
                                    Idle -> null
                                    Syncing -> UiState.Loading()
                                    is SyncFailure -> UiState.Failure(error = result.reason.asUiError())
                                    is SyncSuccess -> UiState.Success(data = result.itemsCount)
                                },
                        )
                    }
                }
            }
        }

        private fun setMimeType(mimeType: String) {
            viewModelState.update {
                it.copy(mimeType = mimeType)
            }
        }

        private fun setQuery(query: String) {
            viewModelState.update {
                it.copy(query = query)
            }
        }

        private fun sendEvent(event: MediaListScreenEvents) {
            viewModelScope.launch {
                viewModelEvent.send(event)
            }
        }
    }
