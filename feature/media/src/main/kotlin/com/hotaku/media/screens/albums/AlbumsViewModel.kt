package com.hotaku.media.screens.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotaku.media.model.AlbumUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AlbumsViewModel
    @Inject
    constructor() : ViewModel() {
        private var albumsViewModelState = MutableStateFlow(AlbumsUiState())
        val albumsState = albumsViewModelState

        private val viewModelEvent = Channel<AlbumsScreenEvents>()
        val event = viewModelEvent.receiveAsFlow()

        fun onAction(action: AlbumsScreenActions) {
            when (action) {
                is AlbumsScreenActions.OnUpdateAlbums -> updateAlbums(action.albums)
                is AlbumsScreenActions.OnAlbumClick -> navigateToMediaScreen(action.albumName)
            }
        }

        private fun updateAlbums(albums: List<AlbumUi>) {
            albumsViewModelState.update {
                it.copy(
                    albums = albums,
                )
            }
        }

        private fun navigateToMediaScreen(albumName: String) {
            viewModelScope.launch {
                viewModelEvent.send(AlbumsScreenEvents.OnNavigateToMediaScreen(albumName))
            }
        }
    }
