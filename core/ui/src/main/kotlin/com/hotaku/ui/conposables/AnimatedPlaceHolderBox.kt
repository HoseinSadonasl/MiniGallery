package com.hotaku.ui.conposables

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun AnimatedPlaceHolderBox(
    modifier: Modifier = Modifier,
) {
    val infiniteTransition: InfiniteTransition =
        rememberInfiniteTransition(label = "infinite color")
    val color: Color by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
        targetValue = MaterialTheme.colorScheme.onSurface.copy(alpha = .15f),
        animationSpec =
            infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "color color",
    )

    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(1f),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(color),
        )
    }
}
