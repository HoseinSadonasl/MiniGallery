package com.hotaku.media_library

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class MediaLibraryViewModel
    @Inject
    constructor() : ViewModel() {
        private var mediaLibraryViewModelState = MutableStateFlow(MediaLibraryUiState())
        val mediaLibraryScreenUiState: StateFlow<MediaLibraryUiState> = mediaLibraryViewModelState.asStateFlow()

        fun onAction(action: MediaLibraryScreenActions) {
        }
    }
