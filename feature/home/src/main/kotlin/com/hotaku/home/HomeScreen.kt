package com.hotaku.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.hotaku.designsystem.theme.LocalMiniGalleryColors
import com.hotaku.feature.home.R
import com.hotaku.home.components.MediaSyncLabel
import com.hotaku.home.model.MediaUi
import com.hotaku.ui.UiState
import com.hotaku.ui.asString
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KFunction1

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    mediaViewModel: MediaViewModel = hiltViewModel(),
    onShowSnackBar: suspend () -> Unit,
) {
    val screenState by mediaViewModel.homeScreenUiState.collectAsStateWithLifecycle()
    val synchronizeState by mediaViewModel.synchronizeUiState.collectAsStateWithLifecycle()

    HomeScreen(
        modifier = modifier,
        screenState = screenState,
        mediaPagingItems = mediaViewModel.mediaUiState,
        synchronizeState = synchronizeState,
        onAction = mediaViewModel::onAction,
    )
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    screenState: HomeScreenUiState,
    mediaPagingItems: StateFlow<PagingData<MediaUi>>,
    synchronizeState: UiState<Int>,
    onAction: KFunction1<HomeScreenActions, Unit>,
) {
    val localColors = LocalMiniGalleryColors.current

    HomeScreenScaffolfd(
        modifier = modifier,
        topBar = {
        },
        bottomBar = {
        },
        snackbarHost = {
        },
        content = {
            when (synchronizeState) {
                is UiState.Failure -> {
                    MediaSyncLabel(
                        backgroundColor = localColors.red,
                        icon = Icons.Default.Warning,
                        label = synchronizeState.error.asString(),
                    )
                }

                is UiState.Loading -> {
                    MediaSyncLabel(
                        backgroundColor = localColors.yellow,
                        isSyncing = true,
                        label = stringResource(R.string.home_screen_state_synchronizing),
                    )
                }

                is UiState.Success -> {
                    MediaSyncLabel(
                        backgroundColor = localColors.yellow,
                        isSyncing = true,
                        icon = Icons.Default.Done,
                        label =
                            stringResource(
                                R.string.home_screen_media_sync_state_media_added,
                                synchronizeState.data,
                            ),
                    )
                }
            }
            MediaGridLazyList(
                mediaPagingItems = mediaPagingItems,
            )
        },
    )
}

@Composable
private fun HomeScreenScaffolfd(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    snackbarHost: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
    ) { padding ->
        Column(
            modifier =
                modifier
                    .padding(padding)
                    .fillMaxSize(),
        ) {
            content()
        }
    }
}

@Composable
private fun MediaGridLazyList(
    modifier: Modifier = Modifier,
    mediaPagingItems: StateFlow<PagingData<MediaUi>>,
    state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
) {
    val items: LazyPagingItems<MediaUi> = mediaPagingItems.collectAsLazyPagingItems()

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(100.dp),
        modifier = modifier,
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        state = state,
    ) {
        mediaItems(items)
    }
}

private fun LazyStaggeredGridScope.mediaItems(items: LazyPagingItems<MediaUi>) {
    items.takeIf { it.itemCount > 0 }?.let { pagingItems ->
        items(
            count = pagingItems.itemCount,
            key = { pagingItems[it]?.mediaId ?: it },
        ) { item ->
            AsyncImage(
                modifier = Modifier.clip(MaterialTheme.shapes.medium),
                model = pagingItems[item]?.uriString,
                contentDescription = null,
            )
        }
    }
}
