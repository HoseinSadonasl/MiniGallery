package com.hotaku.ui.conposables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.hotaku.ui.models.MediaUi

@Composable
fun MediaPreviewPager(
    modifier: Modifier = Modifier,
    currentPage: Int,
    pagerMediaItems: List<MediaUi>,
    onCurrentPageChanged: (Int) -> Unit = {},
    content: @Composable (Int, MediaUi) -> Unit,
) {
    val mediaPagerState =
        rememberPagerState(
            initialPage = currentPage,
            pageCount = { pagerMediaItems.size },
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
        content(page, pagerMediaItems[page])
    }
}
