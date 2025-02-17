package com.hotaku.media.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.hotaku.media.model.MediaUi

@Composable
internal fun MediaPreviewPager(
    modifier: Modifier = Modifier,
    currentPage: Int,
    pagerMediaItems: List<MediaUi>,
    content: @Composable (MediaUi) -> Unit,
) {
    val mediaPagerState =
        rememberPagerState(
            initialPage = currentPage,
            pageCount = { pagerMediaItems.size },
        )

    LaunchedEffect(currentPage) {
        mediaPagerState.animateScrollToPage(currentPage)
    }

    VerticalPager(
        state = mediaPagerState,
        modifier =
            modifier
                .fillMaxSize(),
        key = { it },
    ) { page ->
        content(pagerMediaItems[page])
    }
}
