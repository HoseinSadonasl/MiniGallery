package com.hotaku.media.screens.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.hotaku.domain.utils.DataResult
import com.hotaku.media.mapper.MapAlbumAsAlbumUi
import com.hotaku.media.model.AlbumUi
import com.hotaku.media_domain.usecase.GetAlbumsUseCase
import com.hotaku.ui.UiState
import com.hotaku.ui.asUiError
import com.hotaku.ui.models.MediaUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
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
        private val mapAlbumAsAlbumUi: MapAlbumAsAlbumUi,
    ) : ViewModel() {
        private var albumsViewModelState = MutableStateFlow(AlbumsUiState())
        val albumsUiState =
            albumsViewModelState
                .onStart { updateAlbums() }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = AlbumsUiState(),
                )

        private var mediaViewModelState = MutableStateFlow<PagingData<MediaUi>>(PagingData.empty())
        val mediaUiState = mediaViewModelState.asStateFlow()

        private val albumsDetailsViewModelEvent = Channel<AlbumsScreenEvents>()
        val albumsUiEvent = albumsDetailsViewModelEvent.receiveAsFlow()

        fun onAction(action: AlbumsScreenActions) {
            when (action) {
                is AlbumsScreenActions.OnAlbumClick -> getAlbumMedia(action.album)
                AlbumsScreenActions.OnCloseAlbum -> closeAlbum()
                is AlbumsScreenActions.OnMediaItemClick -> openMediaInDetail(action.mediaItemIndex)
            }
        }

        private fun openMediaInDetail(mediaItemIndex: Int) {
            albumsViewModelState.update {
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
        }

        private fun getAlbumMedia(album: AlbumUi?) {
            albumsViewModelState.update {
                it.copy(
                    selectedAlbum = album,
                )
            }
        }

        fun setMediaState(mediaState: PagingData<MediaUi>) {
            mediaViewModelState.value = mediaState
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
                    albumsViewModelState.update {
                        it.copy(
                            albums = albums,
                        )
                    }
                }
            }
        }
    }
