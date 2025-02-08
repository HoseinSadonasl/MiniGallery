package com.hotaku.media.screens.shared

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.hotaku.media.model.AlbumUi
import com.hotaku.media.model.MediaUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
internal class SharedViewModel
    @Inject
    constructor() : ViewModel() {
        private var mediaViewModelState = MutableStateFlow<PagingData<MediaUi>>(PagingData.empty())
        val mediaUiState = mediaViewModelState

        private var albumsViewModelState = MutableStateFlow(emptyList<AlbumUi>())
        val albumsState = albumsViewModelState

        fun onAction(action: SharedActions) {
            when (action) {
                is SharedActions.OnUpdateMedia -> updateMedia(action.media)
                is SharedActions.OnUpdateAlbums -> updateAlbums(action.albums)
            }
        }

        private fun updateMedia(media: PagingData<MediaUi>) {
            mediaViewModelState.value = media
        }

        private fun updateAlbums(albums: List<AlbumUi>) {
            albumsViewModelState.value = albums
        }
    }
