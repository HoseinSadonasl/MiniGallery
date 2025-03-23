package com.hotaku.ui.conposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hotaku.core_feature.ui.R
import com.hotaku.designsystem.theme.MiniGalleryTheme

@Composable
fun InputDialog(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    inputPlaceHolder: String,
    inputValue: String,
    onInputChange: (String) -> Unit,
    confirmButtonLabel: String = stringResource(id = R.string.dialog_button_label_confirm),
    cancelButtonLabel: String = stringResource(id = R.string.dialog_button_label_cancel),
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    InputDialogImpl(
        modifier = modifier,
        title = title,
        description = description,
        inputValue = inputValue,
        onInputChange = onInputChange,
        confirmButtonLabel = confirmButtonLabel,
        cancelButtonLabel = cancelButtonLabel,
        onConfirm = onConfirm,
        onDismiss = onDismissRequest,
        inputPlaceHolder = inputPlaceHolder,
    )
}

@Composable
private fun InputDialogImpl(
    modifier: Modifier,
    title: String,
    description: String?,
    inputPlaceHolder: String,
    inputValue: String,
    onInputChange: (String) -> Unit,
    confirmButtonLabel: String,
    cancelButtonLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties =
            DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = true,
            ),
    ) {
        Card(
            colors =
                CardDefaults.cardColors().copy(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            Column(
                modifier = modifier.padding(24.dp),
            ) {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
                description?.let {
                    Spacer(Modifier.height(16.dp))
                    Text(text = description)
                }
                Spacer(Modifier.height(16.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    hint = inputPlaceHolder,
                    value = inputValue,
                    onValueChange = onInputChange,
                )
                Spacer(Modifier.height(16.dp))
                Row {
                    Spacer(
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(
                        onClick = onDismiss,
                    ) {
                        Text(text = cancelButtonLabel)
                    }
                    TextButton(
                        onClick = onConfirm,
                    ) {
                        Text(text = confirmButtonLabel)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InputDialogPreview() {
    MiniGalleryTheme {
        InputDialog(
            title = "Title",
            description = LoremIpsum(4).values.first(),
            inputPlaceHolder = "Input Placeholder",
            inputValue = "",
            onInputChange = {},
            onConfirm = {},
            onDismissRequest = {},
        )
    }
}
