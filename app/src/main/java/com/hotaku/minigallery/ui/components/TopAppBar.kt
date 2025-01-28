package com.hotaku.minigallery.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.hotaku.designsystem.theme.MiniGalleryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniGalleryTopBar(
    title: String,
    onNavClick: (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        navigationIcon =
            onNavClick?.let {
                {
                    MinimalIconButton(
                        icon = Icons.AutoMirrored.Rounded.ArrowBack,
                        onClick = onNavClick,
                    )
                }
            } ?: {},
        actions = actions ?: {},
    )
}

@PreviewLightDark
@Composable
private fun MiniGalleryTopBarPreview() {
    MiniGalleryTheme {
        MiniGalleryTopBar(
            title = "Sample title",
            onNavClick = {},
            actions = {
                MinimalIconButton(
                    icon = Icons.Rounded.Star,
                    onClick = {},
                )
                MinimalIconButton(
                    icon = Icons.Rounded.Home,
                    onClick = {},
                )
            },
        )
    }
}
