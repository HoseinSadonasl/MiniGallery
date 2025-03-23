@file:OptIn(ExperimentalMaterial3Api::class)

package com.hotaku.ui.conposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.hotaku.designsystem.theme.MiniGalleryTheme

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onNavBack: (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
) {
    TopAppBarImpl(
        modifier = modifier,
        title = title,
        onNavBack = onNavBack,
        actions = actions,
    )
}

@Composable
private fun TopAppBarImpl(
    modifier: Modifier = Modifier,
    title: String,
    onNavBack: (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CenterAlignedTopAppBar(
            colors =
                TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
                    containerColor = Color.Transparent,
                ),
            navigationIcon = {
                onNavBack?.let {
                    IconButton(
                        onClick = {
                            onNavBack.invoke()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }
            },
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(.6f),
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                )
            },
            actions = { actions?.let { it() } },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarPreview() {
    MiniGalleryTheme {
        TopAppBar(
            title = "All Media",
            actions = {
                IconButton(
                    onClick = {},
                    content = {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null,
                        )
                    },
                )
            },
        )
    }
}
