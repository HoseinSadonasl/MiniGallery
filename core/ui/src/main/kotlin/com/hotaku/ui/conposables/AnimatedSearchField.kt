package com.hotaku.ui.conposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.hotaku.designsystem.theme.MiniGalleryTheme

@Composable
fun AnimatedSearchTextField(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onIconClick: () -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    placeHolderText: String,
) {
    AnimatedSearchTextFieldImpl(
        modifier = modifier,
        expanded = expanded,
        onIconClick = onIconClick,
        value = value,
        onValueChange = onValueChange,
        placeHolderText = placeHolderText,
    )
}

@Composable
private fun AnimatedSearchTextFieldImpl(
    modifier: Modifier,
    expanded: Boolean,
    onIconClick: () -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    placeHolderText: String,
) {
    Box(
        modifier = modifier,
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            TextField(
                modifier =
                    Modifier
                        .align(Alignment.CenterStart),
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
                shape = RoundedCornerShape(percent = 50),
            )
        }

        Spacer(Modifier.width(8.dp))

        IconButton(
            modifier =
                Modifier
                    .align(Alignment.CenterEnd),
            onClick = {
                if (expanded) {
                    onValueChange("")
                }
                onIconClick()
            },
            content = {
                Icon(
                    imageVector = if (expanded) Icons.Outlined.Clear else Icons.Outlined.Search,
                    contentDescription = null,
                )
            },
        )
    }
}

@PreviewLightDark
@Composable
private fun SearchTextFieldPreview() {
    MiniGalleryTheme {
        AnimatedSearchTextField(
            value = "",
            placeHolderText = "placeHolderText",
            onValueChange = {},
            expanded = true,
            onIconClick = {},
        )
    }
}
