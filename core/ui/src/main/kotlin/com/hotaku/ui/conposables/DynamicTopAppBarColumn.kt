package com.hotaku.ui.conposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hotaku.designsystem.theme.MiniGalleryTheme

@Composable
fun DynamicTopAppBarColumn(
    modifier: Modifier = Modifier,
    show: Boolean = true,
    animatableTopContent: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    DynamicTopAppBarColumnImpl(
        modifier = modifier,
        show = show,
        animatableTopContent = animatableTopContent,
        content = content,
    )
}

@Composable
private fun DynamicTopAppBarColumnImpl(
    modifier: Modifier,
    show: Boolean,
    animatableTopContent: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        content = {
            AnimatedVisibility(
                visible = show,
            ) {
                animatableTopContent()
            }
            content()
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun DynamicTopAppBarColumnPreview() {
    MiniGalleryTheme {
        DynamicTopAppBarColumn(
            show = true,
            animatableTopContent = {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Albums",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(Modifier.weight(1f))
                    AnimatedSearchTextField(
                        expanded = true,
                        onIconClick = {},
                        value = "",
                        onValueChange = {},
                        placeHolderText = "Search album",
                    )
                }
            },
            content = { },
        )
    }
}
