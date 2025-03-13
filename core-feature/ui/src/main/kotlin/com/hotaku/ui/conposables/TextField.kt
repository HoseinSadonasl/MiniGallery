package com.hotaku.ui.conposables

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.hotaku.designsystem.theme.MiniGalleryTheme

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    placeHolderText: String,
    value: String,
    onValueChange: (String) -> Unit,
    endIcon: ImageVector? = null,
) {
    TextFieldImpl(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeHolderText = placeHolderText,
        endIcon = endIcon,
    )
}

@Composable
private fun TextFieldImpl(
    modifier: Modifier,
    placeHolderText: String,
    value: String,
    onValueChange: (String) -> Unit,
    endIcon: ImageVector?,
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
        trailingIcon = {
            endIcon?.let {
                Icon(
                    modifier =
                        Modifier.clickable {
                            onValueChange("")
                        },
                    imageVector = if (value.isNotEmpty()) Icons.Outlined.Clear else endIcon,
                    contentDescription = null,
                )
            }
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
            endIcon = Icons.Outlined.Search,
        )
    }
}
