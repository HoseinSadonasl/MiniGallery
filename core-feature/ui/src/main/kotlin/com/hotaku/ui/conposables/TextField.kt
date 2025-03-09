package com.hotaku.ui.conposables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.hotaku.designsystem.theme.MiniGalleryTheme

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    placeHolderText: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextFieldImpl(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeHolderText = placeHolderText,
    )
}

@Composable
private fun TextFieldImpl(
    modifier: Modifier,
    placeHolderText: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        singleLine = true,
        placeholder = {
            Text(
                text = placeHolderText,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        colors =
            TextFieldDefaults.colors().copy(
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
        shape = MaterialTheme.shapes.large,
    )
}

@PreviewLightDark
@Composable
private fun TextFieldPreview() {
    MiniGalleryTheme {
        TextField(
            placeHolderText = "placeholder",
            value = "",
            onValueChange = { },
        )
    }
}
