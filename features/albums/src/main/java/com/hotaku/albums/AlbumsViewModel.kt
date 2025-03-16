package com.hotaku.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.hotaku.albums.AlbumsScreenActions.OnAlbumClick
import com.hotaku.albums.AlbumsScreenActions.OnClearSelectedAlbum
import com.hotaku.albums.AlbumsScreenActions.OnMediaItemClick
import com.hotaku.albums.AlbumsScreenActions.OnSearchFocusChanged
import com.hotaku.albums.AlbumsScreenActions.OnSearchQueryChange
import com.hotaku.albums.AlbumsScreenActions.OnUpdateMediaList
import com.hotaku.albums.mapper.MapAlbumAsAlbumUi
import com.hotaku.albums.model.AlbumUi
import com.hotaku.domain.utils.DataResult
import com.hotaku.media_domain.usecase.GetAlbumsUseCase
import com.hotaku.media_domain.usecase.GetMediaUseCase
import com.hotaku.ui.UiState
import com.hotaku.ui.asUiError
import com.hotaku.ui.mappers.MapMediaAsMediaUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AlbumsViewModel
    @Inject
    constructor(
        private val getAlbumsUseCase: GetAlbumsUseCase,
        private val mediaUseCase: GetMediaUseCase,
        private val mapMediaAsMediaUi: MapMediaAsMediaUi,
        private val mapAlbumAsAlbumUi: MapAlbumAsAlbumUi,
    ) : ViewModel() {
        private var viewModelState = MutableStateFlow(AlbumsUiState())
        val state =
            viewModelState
                .onStart { updateAlbums() }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = AlbumsUiState(),
                )

        private val albumsDetailsViewModelEvent = Channel<AlbumsScreenEvents>()
        val albumsUiEvent = albumsDetailsViewModelEvent.receiveAsFlow()

        fun onAction(action: AlbumsScreenActions) {
            when (action) {
                OnUpdateMediaList -> updateMediaState()
                is OnAlbumClick -> getAlbumMedia(album = action.album)
                OnClearSelectedAlbum -> closeAlbum()
                is OnMediaItemClick -> openMediaInDetail(mediaItemIndex = action.mediaItemIndex)
                is OnSearchQueryChange -> setQuery(query = action.query)
                is OnSearchFocusChanged -> setSearchFocus(hasFocus = action.hasFocus)
            }
        }

        private fun setSearchFocus(hasFocus: Boolean) {
            viewModelState.update {
                it.copy(
                    isSearchFocused = hasFocus,
                )
            }
        }

        private fun setQuery(query: String) {
            viewModelState.update {
                it.copy(
                    query = query,
                )
            }
        }

        private fun updateMediaState() {
            viewModelScope.launch {
                mediaUseCase.invoke(
                    mimeType = viewModelState.value.mimeType,
                    query = viewModelState.value.query,
                    albumName = viewModelState.value.selectedAlbum?.displayName.orEmpty(),
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

        private fun openMediaInDetail(mediaItemIndex: Int) {
            viewModelState.update {
                it.copy(
                    selectedMediaIndex = mediaItemIndex,
                )
            }
            sendEvent(AlbumsScreenEvents.OnNavigateToMediaDetailScreen)
        }

        private fun sendEvent(event: AlbumsScreenEvents) {
            viewModelScope.launch {
                albumsDetailsViewModelEvent.send(event)
            }
        }

        private fun closeAlbum() {
            getAlbumMedia(album = null)
            viewModelState.update {
                it.copy(
                    media = emptyFlow(),
                )
            }
        }

        private fun getAlbumMedia(album: AlbumUi?) {
            viewModelState.update {
                it.copy(
                    selectedAlbum = album,
                )
            }
        }

        private fun updateAlbums() {
            viewModelScope.launch {
                getAlbumsUseCase.invoke().collect { result ->
                    val albums =
                        when (result) {
                            DataResult.Loading -> {
                                UiState.Loading()
                            }
                            is DataResult.Success -> {
                                UiState.Success(
                                    data =
                                        result.data?.map {
                                            mapAlbumAsAlbumUi.map(
                                                it,
                                            )
                                        } ?: emptyList(),
                                )
                            }
                            is DataResult.Failure -> {
                                UiState.Failure(error = result.error.asUiError())
                            }
                        }
                    viewModelState.update {
                        it.copy(
                            albums = albums,
                        )
                    }
                }
            }
        }
    }
