package com.hotaku.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun DecorationMediaImage(
    modifier: Modifier = Modifier,
    image: Painter,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth(.6f),
        contentAlignment = Alignment.Center,
    ) {
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
