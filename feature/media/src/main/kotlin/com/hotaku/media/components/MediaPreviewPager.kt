package com.hotaku.media.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hotaku.media.model.MediaUi
import com.hotaku.media.screens.media_list.MediaListScreenActions

@Composable
internal fun MediaPreviewPager(
    modifier: Modifier = Modifier,
    mediaPagerState: PagerState,
    isCompact: Boolean,
    pagingMediaItems: List<MediaUi>,
    onAction: (MediaListScreenActions) -> Unit,
) {
    VerticalPager(
        state = mediaPagerState,
        modifier = Modifier.fillMaxSize(),
    ) { page ->
        MediaPreviewScaffold(
            modifier = modifier,
            isCompact = isCompact,
            media = pagingMediaItems[page],
            onOpenMedia = {
                onAction(MediaListScreenActions.OnOpenMediaList)
            },
            onDeleteMedia = {
                onAction(MediaListScreenActions.OnDeleteMediaList)
            },
            onShareMedia = {
                onAction(MediaListScreenActions.OnShareMediaList)
            },
            onClosepreview = {
                onAction(MediaListScreenActions.OnClearSelectedMediaList)
            },
        )
    }
}
