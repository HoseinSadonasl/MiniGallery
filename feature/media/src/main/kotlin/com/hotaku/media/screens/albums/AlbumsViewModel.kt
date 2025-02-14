package com.hotaku.media.screens.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.compose.LazyPagingItems
import com.hotaku.domain.utils.DataResult
import com.hotaku.media.mapper.MapAlbumToAlbumUi
import com.hotaku.media.model.AlbumUi
import com.hotaku.media.model.MediaUi
import com.hotaku.media.utils.asUiError
import com.hotaku.media_domain.usecase.GetAlbumsUseCase
import com.hotaku.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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
        private val mapAlbumToAlbumUi: MapAlbumToAlbumUi,
    ) : ViewModel() {
        private var albumsViewModelState = MutableStateFlow(AlbumsUiState())
        val albumsState =
            albumsViewModelState
                .onStart { updateAlbums() }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = AlbumsUiState(),
                )

        private val viewModelEvent = Channel<AlbumsScreenEvents>()
        val event = viewModelEvent.receiveAsFlow()

        fun onAction(action: AlbumsScreenActions) {
            when (action) {
                is AlbumsScreenActions.OnAlbumClick -> getAlbumMedia(action.album)
                is AlbumsScreenActions.OnOpenAlbum -> onAddMedia(action.media)
                AlbumsScreenActions.OnCloseAlbum -> closeAlbum()
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

        private fun onAddMedia(media: LazyPagingItems<MediaUi>?) {
            albumsViewModelState.update {
                it.copy(
                    mediaList = media,
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
                                            mapAlbumToAlbumUi.map(
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
