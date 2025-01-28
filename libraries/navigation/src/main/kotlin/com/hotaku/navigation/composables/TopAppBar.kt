package com.hotaku.navigation.composables

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

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
                }
            } ?: {},
        actions = actions ?: {},
    )
}

@Preview
@Composable
private fun MiniGalleryTopBarPreview() {
}
