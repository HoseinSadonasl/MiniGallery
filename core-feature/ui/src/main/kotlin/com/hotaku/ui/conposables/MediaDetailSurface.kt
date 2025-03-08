package com.hotaku.ui.conposables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hotaku.designsystem.theme.MiniGalleryTheme

@Composable
fun MediaDetailSurface(
    modifier: Modifier = Modifier,
    topContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    MediaDetailSurfaceImpl(
        modifier = modifier,
        topContent = topContent,
        content = content,
    )
}

@Composable
private fun MediaDetailSurfaceImpl(
    modifier: Modifier,
    topContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(modifier = Modifier.matchParentSize()) { content() }
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) { topContent() }
    }
}

@Preview(showBackground = true)
@Composable
private fun MediaDetailSurfacePreview() {
    MiniGalleryTheme {
        MediaDetailSurface(
            topContent = {
                AnimatedMediaDetailCompactTopBar(
                    show = true,
                    title = "MediaName.png",
                    onClose = {},
                )
            },
            content = {},
        )
    }
}
