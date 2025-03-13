package com.hotaku.ui.conposables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hotaku.designsystem.theme.MiniGalleryTheme

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBarImpl(
        modifier = modifier,
        title = title,
        content = content,
        actions = actions,
    )
}

@Composable
private fun TopAppBarImpl(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable (() -> Unit)?,
    actions: @Composable (RowScope.() -> Unit)?,
) {
    Column(
        modifier =
            modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            content?.let {
                Column(
                    modifier = Modifier.fillMaxWidth(.6f).align(Alignment.Center),
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    content()
                }
            }
            actions?.let {
                Row(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    actions()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarPreview() {
    MiniGalleryTheme {
        TopAppBar(
            title = "All Media",
            content = {
                TextField(
                    modifier = Modifier,
                    value = "",
                    onValueChange = {},
                    placeHolderText = "Search album",
                )
            },
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
