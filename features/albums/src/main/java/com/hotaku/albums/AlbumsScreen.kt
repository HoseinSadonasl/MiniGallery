@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.hotaku.albums

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.albums.AlbumsScreenActions.OnAlbumClick
import com.hotaku.albums.AlbumsScreenActions.OnClearSelectedAlbum
import com.hotaku.albums.AlbumsScreenActions.OnListIsScrolling
import com.hotaku.albums.AlbumsScreenActions.OnMediaItemClick
import com.hotaku.albums.AlbumsScreenActions.OnSearchQueryChange
import com.hotaku.albums.AlbumsScreenActions.OnSetTopBarVisibility
import com.hotaku.albums.AlbumsScreenActions.OnUpdateMediaList
import com.hotaku.albums.model.AlbumUi
import com.hotaku.features.albums.R
import com.hotaku.ui.MediaType
import com.hotaku.ui.UiState
import com.hotaku.ui.conposables.AnimatedFloatSearch
import com.hotaku.ui.conposables.DynamicTopAppBarColumn
import com.hotaku.ui.conposables.EmptyPaneMessage
import com.hotaku.ui.conposables.ImageThumbnail
import com.hotaku.ui.conposables.MediaGrid
import com.hotaku.ui.conposables.MediaPlaceHolder
import com.hotaku.ui.conposables.OnScreenMessage
import com.hotaku.ui.conposables.TopAppBar
import com.hotaku.ui.conposables.VideoThumbnail
import com.hotaku.ui.models.MediaUi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AlbumsScreen(
    modifier: Modifier = Modifier,
    navigateToMediaDetailScreen: (Int?, String) -> Unit,
) {
    val navigator = rememberSupportingPaneScaffoldNavigator<String>()
    val albumsViewModel = hiltViewModel<AlbumsViewModel>()
    val state by albumsViewModel.state.collectAsStateWithLifecycle()
    val media = state.media.collectAsLazyPagingItems()

    LaunchedEffect(albumsViewModel.event) {
        albumsViewModel.event.collectLatest { event ->
            when (event) {
                AlbumsScreenEvents.OnOpenAlbum -> {
                    state.selectedAlbum?.let {
                        albumsViewModel.onAction(OnUpdateMediaList)
                        navigator.navigateTo(ThreePaneScaffoldRole.Secondary, it.displayName)
                    }
                }

                AlbumsScreenEvents.OnNavigateToMediaDetailScreen -> {
                    state.selectedAlbum?.displayName?.let { selectedAlbum ->
                        navigateToMediaDetailScreen(state.selectedMediaIndex, selectedAlbum)
                    }
                }
            }
        }
    }

    AlbumsScreenContent(
        modifier = modifier,
        navigator = navigator,
        state = state,
        pagingMediaItems = media,
        onAction = albumsViewModel::onAction,
    )
}

@Composable
private fun AlbumsScreenContent(
    modifier: Modifier = Modifier,
    navigator: ThreePaneScaffoldNavigator<String>,
    state: AlbumsUiState,
    pagingMediaItems: LazyPagingItems<MediaUi>,
    onAction: (AlbumsScreenActions) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass
    val isCompact = windowSize.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    val scope = rememberCoroutineScope()

    BackHandler(navigator.canNavigateBack()) {
        onAction(OnClearSelectedAlbum)
        scope.launch {
            navigator.navigateBack()
        }
    }

    BackHandler(state.isSearchFocused) {
        onAction(OnSearchQueryChange(query = ""))
        focusManager.clearFocus()
    }

    LaunchedEffect(state.query) {
        onAction(OnUpdateMediaList)
    }

    DynamicTopAppBarColumn(
        modifier = modifier,
        show = state.isTopBarVisible,
        animatableTopContent = {
            AnimatedContent(targetState = state.selectedAlbum) { selectedAlbum ->
                TopAppBar(
                    title =
                        state.selectedAlbum?.displayName
                            ?: stringResource(R.string.albums_screen_top_app_bar_title),
                    actions = {
                        selectedAlbum?.let {
                            IconButton(
                                onClick = {
                                    onAction(OnClearSelectedAlbum)
                                    scope.launch {
                                        navigator.navigateBack()
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Close,
                                    contentDescription = null,
                                )
                            }
                        }
                    },
                )
//                selectedAlbum?.let {
//                    TextField(
//                        modifier =
//                            Modifier
//                                .fillMaxWidth()
//                                .onFocusChanged {
//                                    onAction(OnSearchFocusChanged(hasFocus = it.hasFocus))
//                                },
//                        value = state.query,
//                        onValueChange = { query ->
//                            onAction(OnSearchQueryChange(query = query))
//                        },
//                        hint = stringResource(R.string.albums_screen_search_media),
//                        endIcon = Icons.Outlined.Search,
//                    )
//                }
            }
        },
        content = {
            SupportingPaneScaffold(
                directive =
                    navigator.scaffoldDirective.copy(
                        horizontalPartitionSpacerSize = 8.dp,
                    ),
                value = navigator.scaffoldValue,
                mainPane = {
                    AnimatedPane {
                        AlbumsGridList(
                            albumsListState = state.albums,
                            isCompact = isCompact,
                            onAction = onAction,
                        )
                    }
                },
                supportingPane = {
                    AnimatedPane {
                        navigator.currentDestination?.contentKey?.let { albumName ->
                            state.selectedAlbum?.let {
                                SupportingPaneContent(
                                    pagingMediaItems = pagingMediaItems,
                                    onAction = onAction,
                                    isCompact = isCompact,
                                    state = state,
                                )
                            }
                        } ?: Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            EmptyPaneMessage(
                                icon = Icons.Outlined.Info,
                                message = stringResource(R.string.albums_screen_empty_pane_message),
                            )
                        }
                    }
                },
            )
        },
    )
}

@Composable
private fun SupportingPaneContent(
    pagingMediaItems: LazyPagingItems<MediaUi>,
    onAction: (AlbumsScreenActions) -> Unit,
    isCompact: Boolean,
    state: AlbumsUiState,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        MediaGrid(
            pagingMediaItems = pagingMediaItems,
            onScrolled = { isScrolling ->
                onAction(OnListIsScrolling(isScrolling = isScrolling))
            },
            isFirstItemVisible = { isVisible ->
                if (isCompact) {
                    onAction(OnSetTopBarVisibility(visible = isVisible))
                }
            },
            onItemClick = { itemIndex ->
                onAction(OnMediaItemClick(mediaItemIndex = itemIndex))
            },
            onItemLongClick = {},
        )
        AnimatedFloatSearch(
            modifier =
                Modifier
                    .statusBarsPadding()
                    .padding(16.dp),
            visible = !state.isScrolling && !state.isTopBarVisible,
            value = state.query,
            onValueChange = { query ->
                onAction(OnSearchQueryChange(query = query))
            },
        )
    }
}

@Composable
private fun NoAlbums() {
    OnScreenMessage(
        modifier = Modifier.fillMaxSize(),
        title = stringResource(id = R.string.albums_screen_no_albums),
        fulMessage = stringResource(id = R.string.albums_screen_no_albums_full_message),
    )
}

@Composable
private fun AlbumsLoadError() {
    OnScreenMessage(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.error,
        title = stringResource(id = R.string.albums_screen_error_while_getting_albums),
        fulMessage = stringResource(id = R.string.albums_screen_error_while_getting_albums_full_message),
    )
}

@Composable
private fun AlbumsGridList(
    modifier: Modifier = Modifier,
    isCompact: Boolean,
    albumsListState: UiState<List<AlbumUi>>,
    onAction: (AlbumsScreenActions) -> Unit,
) {
    when {
        albumsListState is UiState.Failure -> {
            AlbumsLoadError()
        }
        albumsListState is UiState.Success && albumsListState.data.isEmpty() -> {
            NoAlbums()
        }
        else -> {
            LazyVerticalGrid(
                modifier = modifier.fillMaxSize(),
                columns = GridCells.Adaptive(120.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = if (isCompact) 16.dp else 0.dp),
            ) {
                when (albumsListState) {
                    is UiState.Loading -> {
                        items(6) {
                            MediaPlaceHolder(modifier = Modifier.clip(MaterialTheme.shapes.medium))
                        }
                    }
                    is UiState.Success -> {
                        albumsListItems(
                            albumsListState = albumsListState,
                            onAlbumClick = onAction,
                        )
                    }
                    else -> {
                        // Do nothing
                    }
                }
            }
        }
    }
}

private fun LazyGridScope.albumsListItems(
    albumsListState: UiState.Success<List<AlbumUi>>,
    onAlbumClick: (AlbumsScreenActions) -> Unit,
) {
    items(
        items = albumsListState.data,
        key = { it.thumbnailUriString },
    ) { album ->
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { onAlbumClick(OnAlbumClick(album)) },
        ) {
            when (album.thumbnailType) {
                MediaType.IMAGE -> {
                    ImageThumbnail(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .clip(MaterialTheme.shapes.medium),
                        itemUri = album.thumbnailUriString,
                    )
                }

                MediaType.VIDEO -> {
                    VideoThumbnail(
                        Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.medium),
                        itemUri = album.thumbnailUriString,
                    )
                }

                else -> {
                    MediaPlaceHolder(modifier = Modifier.clip(MaterialTheme.shapes.medium))
                }
            }
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(MaterialTheme.colorScheme.background.copy(alpha = .7f))
                        .padding(4.dp),
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = album.displayName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = album.count.toString(),
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}
