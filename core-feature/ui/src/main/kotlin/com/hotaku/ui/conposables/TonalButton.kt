package com.hotaku.ui.conposables

import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hotaku.designsystem.theme.MiniGalleryTheme

@Composable
fun TonalButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    TonalButtonImpl(
        modifier = modifier,
        text = text,
        onClick = onClick,
    )
}

@Composable
private fun TonalButtonImpl(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    FilledTonalButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
private fun TonalButtonPreview() {
    MiniGalleryTheme {
        TonalButton(
            text = "Tonal Button",
            onClick = {},
        )
    }
}
