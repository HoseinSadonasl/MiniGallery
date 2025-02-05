package com.hotaku.home

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.feature.home.R
import com.hotaku.home.components.MediaSyncLabel
import com.hotaku.home.components.ScreenMessage
import com.hotaku.home.model.MediaUi
import com.hotaku.ui.UiState
import com.hotaku.ui.UiText
import com.hotaku.ui.asString
import com.hotaku.ui.conposables.AnimatedPlaceHolderBox
import com.hotaku.ui.conposables.AnimatedSearchTextField
import com.hotaku.ui.conposables.DynamicTopAppBarColumn
import com.hotaku.ui.conposables.TopAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    mediaViewModel: MediaViewModel = hiltViewModel(),
    onShowSnackBar: suspend (String) -> Unit,
) {
    HomeScreen(
        modifier = modifier,
        screenState = mediaViewModel.homeScreenUiState,
        pagingMediaItemsState = mediaViewModel.mediaUiState,
        synchronizeState = mediaViewModel.synchronizeUiState,
        onAction = mediaViewModel::onAction,
        onShowSnackBar = { onShowSnackBar(it) },
    )
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    screenState: StateFlow<HomeScreenUiState>,
    pagingMediaItemsState: StateFlow<PagingData<MediaUi>>,
    synchronizeState: StateFlow<UiState<Int>>,
    onAction: (HomeScreenActions) -> Unit,
    onShowSnackBar: suspend (String) -> Unit,
) {
    val state: HomeScreenUiState by screenState.collectAsStateWithLifecycle()
    val synchronize: UiState<Int> by synchronizeState.collectAsStateWithLifecycle()
    val pagingMediaItems: LazyPagingItems<MediaUi> = pagingMediaItemsState.collectAsLazyPagingItems()

    val focusManager = LocalFocusManager.current

    BackHandler(state.isSearchExpanded) {
        focusManager.clearFocus()
        onAction(HomeScreenActions.OnQueryChange(query = ""))
        onAction(HomeScreenActions.OnCollepseSearch)
    }

    LaunchedEffect(state.query) {
        onAction(HomeScreenActions.OnUpdateMedia)
    }

    LaunchedEffect(synchronize) {
        if (synchronize is UiState.Success) {
            pagingMediaItems.refresh()
            delay(3000)
            onAction(HomeScreenActions.OnHideSyncSection)
        }
    }

    DynamicTopAppBarColumn(
        modifier = modifier,
        show = !state.isScrolled,
        animatableTopContent = {
            TopAppBar(
                title = stringResource(R.string.home_screentop_bar_title_all_media),
                content = {
                    AnimatedSearchTextField(
                        expanded = state.isSearchExpanded,
                        onIconClick = {
                            if (state.isSearchExpanded && state.query.isEmpty()) {
                                onAction(HomeScreenActions.OnCollepseSearch)
                            } else {
                                onAction(HomeScreenActions.OnExpandSearch)
                            }
                        },
                        value = state.query,
                        onValueChange = { query ->
                            onAction(HomeScreenActions.OnQueryChange(query = query))
                        },
                        placeHolderText = stringResource(R.string.home_screen_search_media),
                    )
                },
            )
        },
        content = {
            AnimatedVisibility(
                visible = state.showSyncSection,
            ) {
                SyncSection(synchronize)
            }
            if (synchronize is UiState.Success && pagingMediaItems.itemCount == 0) {
                NoMedia()
            } else {
                MediaGridLazyList(
                    modifier = Modifier.weight(1f),
                    pagingMediaItems = pagingMediaItems,
                    onScrolled = { scrolled ->
                        onAction(HomeScreenActions.OnScrolled(isScrolled = scrolled))
                    },
                    onShowSnackBar = { onShowSnackBar(it) },
                )
            }
        },
    )
}

@Composable
private fun NoMedia() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        ScreenMessage(
            title = stringResource(id = R.string.home_screen_no_media),
            fulMessage = stringResource(id = R.string.home_screen_no_media_full_message),
        )
    }
}

@Preview(showBackground = true)
@PreviewScreenSizes
@Composable
private fun NoMediaPreview() {
    MiniGalleryTheme {
        NoMedia()
    }
}

@Composable
private fun SyncSection(
    synchronizeState: UiState<Int>,
) {
    when (synchronizeState) {
        is UiState.Failure -> {
            MediaSyncLabel(
                icon = Icons.Default.Warning,
                label = synchronizeState.error.asString(),
            )
        }

        is UiState.Loading -> {
            MediaSyncLabel(
                isSyncing = true,
                label = stringResource(R.string.home_screen_state_synchronizing),
            )
        }

        is UiState.Success -> {
            MediaSyncLabel(
                icon = Icons.Default.Done,
                label =
                    stringResource(
                        R.string.home_screen_media_sync_state_media_added,
                        synchronizeState.data,
                    ),
            )
        }
    }
}

@Composable
private fun MediaGridLazyList(
    modifier: Modifier = Modifier,
    pagingMediaItems: LazyPagingItems<MediaUi>,
    onScrolled: (Boolean) -> Unit,
    onShowSnackBar: suspend (String) -> Unit,
) {
    val context: Context = LocalContext.current
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val loadState: LoadState = pagingMediaItems.loadState.refresh

    val lazyGridState: LazyGridState = rememberLazyGridState()
    val scrolled by remember {
        derivedStateOf { lazyGridState.firstVisibleItemIndex > 0 }
    }

    LaunchedEffect(scrolled) {
        onScrolled(scrolled)
    }

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(100.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = PaddingValues(2.dp),
        state = lazyGridState,
    ) {
        when (loadState) {
            LoadState.Loading -> {
                items(30) {
                    AnimatedPlaceHolderBox()
                }
            }

            is LoadState.Error -> {
                val errorMessage: String? = loadState.error.localizedMessage
                coroutineScope.showLoadingError(
                    onShowSnackBar = onShowSnackBar,
                    message = errorMessage,
                    context = context,
                )
            }

            else -> {
                mediaItems(pagingMediaItems)
            }
        }
    }
}

private fun CoroutineScope.showLoadingError(
    onShowSnackBar: suspend (String) -> Unit,
    message: String?,
    context: Context,
) {
    launch {
        onShowSnackBar(
            message ?: UiText.StringResource(R.string.home_screen_an_error_occurred)
                .asString(context = context),
        )
    }
}

private fun LazyGridScope.mediaItems(items: LazyPagingItems<MediaUi>) {
    items.takeIf { it.itemCount > 0 }?.let { pagingItems ->
        items(
            count = pagingItems.itemCount,
            key = { it },
        ) { item ->
            Surface(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .aspectRatio(1f),
            ) {
                AsyncImage(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                    model = pagingItems[item]?.uriString,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
            }
        }
    }
}
