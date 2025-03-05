package com.hotaku.ui.conposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.hotaku.ui.asThumbnailImageBitmap

@Composable
internal fun Video(
    modifier: Modifier = Modifier,
    itemUri: String,
) {
    val thumbnail = itemUri.toUri().asThumbnailImageBitmap()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            bitmap = thumbnail,
            contentDescription = null,
            modifier = modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
        )
        Icon(
            modifier =
                Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .alpha(.6f),
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = "video",
            tint = Color.Gray,
        )
    }
}

@Composable
internal fun Image(
    modifier: Modifier = Modifier,
    itemUri: String,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        AsyncImage(
            modifier =
                Modifier
                    .fillMaxSize(),
            model = itemUri,
            contentDescription = null,
        )
    }
}
