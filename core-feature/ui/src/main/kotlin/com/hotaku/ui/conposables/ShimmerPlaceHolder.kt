package com.hotaku.ui.conposables

import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import com.hotaku.designsystem.theme.MiniGalleryTheme

@Composable
fun ShimmerPlaceHolder(modifier: Modifier = Modifier) {
    val infiniteTransition: InfiniteTransition =
        rememberInfiniteTransition(label = "infinite color")
    val shimmerPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
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
                .background(
                    Brush.linearGradient(
                        0f to MaterialTheme.colorScheme.surfaceDim,
                        shimmerPosition to MaterialTheme.colorScheme.surfaceContainer,
                        1f to MaterialTheme.colorScheme.surfaceDim,
                    ),
                ),
        )
    }
}

@Preview
@Composable
private fun AnimatedPlaceHolderBoxPreview() {
    MiniGalleryTheme {
        ShimmerPlaceHolder()
    }
}
