package com.hotaku.ui.conposables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
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
    content: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier =
            modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.width(16.dp))
        Box(
            modifier =
                Modifier
                    .weight(1f),
            contentAlignment = Alignment.CenterEnd,
        ) {
            content()
        }
        actions()
    }
}

@Preview(showBackground = true)
@PreviewScreenSizes
@Composable
private fun TopAppBarPreview() {
    MiniGalleryTheme {
        TopAppBar(
            title = "Sample title",
            content = {
                AnimatedSearchTextField(
                    modifier = Modifier,
                    expanded = true,
                    onIconClick = {},
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
