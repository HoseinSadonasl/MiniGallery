package com.hotaku.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun DecorationImage(
    modifier: Modifier = Modifier,
    image: Painter,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth(.6f),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .padding(24.dp)
                    .matchParentSize()
                    .clip(RoundedCornerShape(percent = 100))
                    .alpha(.1f)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.surface,
                            ),
                        ),
                    ),
        )
        Image(
            modifier =
                Modifier
                    .aspectRatio(1f),
            painter = image,
            contentDescription = null,
        )
    }
}
