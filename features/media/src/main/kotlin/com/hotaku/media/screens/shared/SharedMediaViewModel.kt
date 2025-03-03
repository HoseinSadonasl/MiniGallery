package com.hotaku.media.screens.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.hotaku.media.mapper.MapMediaAsMediaUi
import com.hotaku.media_domain.usecase.GetMediaUseCase
import com.hotaku.ui.models.MediaUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SharedMediaViewModel
    @Inject
    constructor(
        private val mediaUseCase: GetMediaUseCase,
        private val mapMediaAsMediaUi: MapMediaAsMediaUi,
    ) : ViewModel() {
        private var mediaViewModelState = MutableStateFlow<PagingData<MediaUi>>(PagingData.empty())
        val mediaUiState =
            mediaViewModelState
                .onStart { updateMediaState() }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = PagingData.empty(),
                )

        fun updateMediaState(
            mimeType: String = "",
            query: String = "",
            albumName: String = "",
        ) {
            viewModelScope.launch {
                mediaUseCase.invoke(
                    mimeType = mimeType,
                    query = query,
                    albumName = albumName,
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
    }
