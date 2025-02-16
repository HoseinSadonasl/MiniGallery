package com.hotaku.media.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.hotaku.feature.media.R
import com.hotaku.media.model.MediaUi
import com.hotaku.media.utils.MediaType
import com.hotaku.ui.conposables.ShimmerPlaceHolder
import kotlin.random.Random

@Composable
internal fun MediaGrid(
    modifier: Modifier = Modifier,
    pagingMediaItems: LazyPagingItems<MediaUi>,
    onScrolled: (Boolean) -> Unit,
    onItemClick: (Int) -> Unit,
    onItemLongClick: () -> Unit,
) {
    val loadState: LoadState = pagingMediaItems.loadState.refresh

    val lazyGridState: LazyGridState = rememberLazyGridState()
    val scrolled by remember {
        derivedStateOf { lazyGridState.firstVisibleItemIndex > 0 }
    }

    LaunchedEffect(scrolled) {
        onScrolled(scrolled)
    }

    if (loadState is LoadState.Error) {
        val errorMessage: String? = loadState.error.localizedMessage
        OnScreenMessage(
            title = stringResource(id = R.string.albums_screen_error_while_getting_media),
            fulMessage = errorMessage ?: stringResource(id = R.string.media_grid_list_an_error_occured),
        )
    } else {
        MediaGridList(
            modifier = modifier,
            lazyGridState = lazyGridState,
            loadState = loadState,
            items = pagingMediaItems,
            onItemClick = onItemClick,
            onItemLongClick = onItemLongClick,
        )
    }
}

@Composable
private fun MediaGridList(
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState,
    loadState: LoadState,
    items: LazyPagingItems<MediaUi>,
    onItemClick: (Int) -> Unit,
    onItemLongClick: () -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(80.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = PaddingValues(2.dp),
        state = lazyGridState,
    ) {
        when (loadState) {
            LoadState.Loading -> {
                items(50) {
                    ShimmerPlaceHolder()
                }
            }

            else -> {
                mediaItems(
                    items = items,
                    onItemClick = onItemClick,
                    onItemLongClick = onItemLongClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyGridScope.mediaItems(
    items: LazyPagingItems<MediaUi>,
    onItemClick: (Int) -> Unit,
    onItemLongClick: () -> Unit,
) {
    items(
        count = items.itemCount,
        key = { items[it]?.mediaId ?: Random.nextInt() },
    ) { index ->
        items[index]?.let { item ->
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(MaterialTheme.colorScheme.surfaceDim)
                        .combinedClickable(
                            onClick = { items[index]?.let { onItemClick(index) } },
                            onLongClick = onItemLongClick,
                        ),
            ) {
                when (item.mimeType) {
                    MediaType.UNKNOWN -> { /* Unknown file thumbnail will show nothing */ }
                    MediaType.IMAGE -> {
                        ImageThumbnail(itemUri = item.uriString)
                    }
                    MediaType.VIDEO -> {
                        VideoThumbnail(item)
                    }
                }
            }
        }
    }
}
