package com.hotaku.media.screens.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotaku.media.mapper.MapAlbumToAlbumUi
import com.hotaku.media.model.AlbumUi
import com.hotaku.media_domain.usecase.GetAlbumsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AlbumsViewModel
    @Inject
    constructor(
        private val getAlbumsUseCase: GetAlbumsUseCase,
        private val mapAlbumToAlbumUi: MapAlbumToAlbumUi,
    ) : ViewModel() {
        private var albumsViewModelState = MutableStateFlow(emptyList<AlbumUi>())
        val albumsState =
            albumsViewModelState
                .onStart {
                    getAlbumsUseCase.invoke().map { mapAlbumToAlbumUi.map(it) }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList(),
                )

        private val viewModelEvent = Channel<AlbumsScreenEvents>()
        val event = viewModelEvent.receiveAsFlow()

        fun onAction(action: AlbumsScreenActions) {
            when (action) {
                is AlbumsScreenActions.OnAlbumClick -> navigateToMediaScreen(action.album)
            }
        }

        private fun navigateToMediaScreen(album: AlbumUi) {
            viewModelScope.launch {
                viewModelEvent.send(AlbumsScreenEvents.OnNavigateToWithAlbum(album))
            }
        }
    }
