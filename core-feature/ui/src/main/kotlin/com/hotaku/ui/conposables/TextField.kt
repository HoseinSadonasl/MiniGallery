package com.hotaku.ui.conposables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.hotaku.designsystem.theme.MiniGalleryTheme

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    endIcon: ImageVector? = null,
    isError: Boolean = false,
    singleLine: Boolean = true,
) {
    TextFieldImpl(
        modifier = modifier,
        hint = hint,
        value = value,
        onValueChange = onValueChange,
        endIcon = endIcon,
        isError = isError,
        singleLine = singleLine,
    )
}

@Composable
private fun TextFieldImpl(
    modifier: Modifier = Modifier,
    hint: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    endIcon: ImageVector? = null,
    isError: Boolean = false,
    singleLine: Boolean = true,
) {
    AppTextField(
        modifier = modifier,
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        hint = hint,
        endIcon = endIcon,
        isError = isError,
        singleLine = singleLine,
    )
}

@PreviewLightDark
@Composable
private fun TextFieldPreview() {
    MiniGalleryTheme {
        TextField(
            hint = "Placeholder",
            value = "",
            onValueChange = { },
            endIcon = Icons.Outlined.Search,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String? = null,
    endIcon: ImageVector? = null,
    isError: Boolean = false,
    singleLine: Boolean = true,
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    val colors: TextFieldColors =
        TextFieldDefaults.colors().copy(
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )

    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicTextField(
            value = value,
            modifier =
                modifier
                    .defaultMinSize(
                        minWidth = TextFieldDefaults.MinWidth,
                        minHeight = 48.dp,
                    ),
            onValueChange = onValueChange,
            enabled = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions.Default,
            keyboardActions = KeyboardActions.Default,
            interactionSource = interactionSource,
            singleLine = singleLine,
            maxLines = if (singleLine) 1 else Int.MAX_VALUE,
            minLines = 1,
            decorationBox =
                @Composable { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = value,
                        visualTransformation = VisualTransformation.None,
                        innerTextField = innerTextField,
                        placeholder = {
                            BasicText(
                                text = hint.orEmpty(),
                                style = MaterialTheme.typography.bodyMedium,
                                color =
                                    ColorProducer {
                                        colors.disabledPlaceholderColor
                                    },
                            )
                        },
                        trailingIcon = {
                            endIcon?.let {
                                Icon(
                                    modifier =
                                        Modifier
                                            .clip(RoundedCornerShape(percent = 50))
                                            .clickable {
                                                if (value.isNotEmpty()) onValueChange("")
                                            },
                                    tint = if (value.isEmpty()) colors.disabledPlaceholderColor else MaterialTheme.colorScheme.onBackground,
                                    imageVector = if (value.isNotEmpty()) Icons.Outlined.Clear else it,
                                    contentDescription = null,
                                )
                            }
                        },
                        shape = MaterialTheme.shapes.large,
                        singleLine = singleLine,
                        enabled = true,
                        isError = isError,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        interactionSource = interactionSource,
                        colors = colors,
                    )
                },
        )
    }
}
