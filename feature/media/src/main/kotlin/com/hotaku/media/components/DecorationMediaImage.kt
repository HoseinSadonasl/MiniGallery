package com.hotaku.media.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.hotaku.feature.media.R

@Composable
fun DecorationMediaImage(
    modifier: Modifier = Modifier,
    image: Painter,
    isError: Boolean = false,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth(.6f),
        contentAlignment = Alignment.Center,
    ) {
        if (isError) {
            Icon(
                modifier =
                    Modifier
                        .size(48.dp)
                        .align(Alignment.TopStart).alpha(.5f),
                imageVector = ImageVector.vectorResource(id = R.drawable.all_error),
                tint = MaterialTheme.colorScheme.error,
                contentDescription = null,
            )
        }
        Image(
            modifier =
                Modifier
                    .aspectRatio(1f)
                    .alpha(.5f),
            painter = image,
            contentDescription = null,
        )
    }
}
