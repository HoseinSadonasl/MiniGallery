package com.hotaku.ui.conposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DynamicTopAppBarScaffold(
    modifier: Modifier = Modifier,
    show: Boolean = true,
    topAppBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AnimatedVisibility(
                visible = show,
                enter = slideInVertically(animationSpec = tween(1500)),
                exit = slideOutVertically(animationSpec = tween(1000)),
            ) {
                topAppBar()
            }
        },
        content = { paddingValues ->
            content(paddingValues)
        },
    )
}
