package com.hotaku.ui.conposables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.hotaku.ui.models.MediaUi

@Composable
fun MediaPreviewPager(
    modifier: Modifier = Modifier,
    currentPage: Int,
    pagerMediaItems: LazyPagingItems<MediaUi>,
    onCurrentPageChanged: (Int) -> Unit = {},
    content: @Composable (MediaUi) -> Unit,
) {
    MediaPreviewPagerImpl(
        modifier = modifier,
        currentPage = currentPage,
        pagerMediaItems = pagerMediaItems,
        onCurrentPageChanged = onCurrentPageChanged,
        content = content,
    )
}

@Composable
private fun MediaPreviewPagerImpl(
    modifier: Modifier = Modifier,
    currentPage: Int,
    pagerMediaItems: LazyPagingItems<MediaUi>,
    onCurrentPageChanged: (Int) -> Unit = {},
    content: @Composable (MediaUi) -> Unit,
) {
    val mediaPagerState =
        rememberPagerState(
            initialPage = currentPage,
            pageCount = { pagerMediaItems.itemCount },
        )

    LaunchedEffect(currentPage) {
        mediaPagerState.animateScrollToPage(currentPage)
    }

    LaunchedEffect(mediaPagerState.currentPage) {
        onCurrentPageChanged(mediaPagerState.currentPage)
    }

    VerticalPager(
        state = mediaPagerState,
        modifier =
            modifier
                .fillMaxSize(),
        key = { it },
    ) { page ->
        pagerMediaItems[page]?.let { content(it) }
    }
}
