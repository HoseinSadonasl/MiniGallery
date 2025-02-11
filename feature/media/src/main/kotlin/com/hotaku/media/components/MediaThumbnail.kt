package com.hotaku.media.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.hotaku.feature.media.R
import com.hotaku.media.model.MediaUi
import com.hotaku.media.utils.asThumbnailImageBitmap

@Composable
internal fun ImageThumbnail(
    modifier: Modifier = Modifier,
    itemUri: String,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(1f),
    ) {
        AsyncImage(
            modifier =
                Modifier
                    .fillMaxSize(),
            model = itemUri,
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
    }
}

@Composable
fun VideoThumbnail(item: MediaUi) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
    ) {
        VideoThumbnail(
            modifier = Modifier.matchParentSize(),
            itemUri = item.uriString,
        )
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd)
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            1f to Color.Black.copy(alpha = .5f),
                        ),
                    )
                    .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.all_video),
                tint = Color.White,
                contentDescription = "video",
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = item.duration,
                style =
                    MaterialTheme.typography.labelSmall.copy(
                        color = Color.White,
                    ),
            )
        }
    }
}

@Composable
fun VideoThumbnail(
    modifier: Modifier = Modifier,
    itemUri: String,
) {
    val thumbnail = itemUri.toUri().asThumbnailImageBitmap()

    Image(
        bitmap = thumbnail,
        contentDescription = null,
        modifier =
            modifier
                .aspectRatio(1f),
        contentScale = ContentScale.Crop,
    )
}
