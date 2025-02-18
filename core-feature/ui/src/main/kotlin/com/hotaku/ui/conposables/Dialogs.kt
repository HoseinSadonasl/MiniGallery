package com.hotaku.ui.conposables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.hotaku.core_feature.ui.R

@Composable
fun AlertDialog(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String,
    description: String,
    confirmButtonLabel: String = stringResource(id = R.string.dialog_button_label_confirm),
    cancelButtonLabel: String = stringResource(id = R.string.dialog_button_label_cancel),
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialogImpl(
        modifier = modifier,
        icon = icon,
        title = title,
        description = description,
        confirmButtonLabel = confirmButtonLabel,
        cancelButtonLabel = cancelButtonLabel,
        onDismiss = onDismiss,
        onConfirm = onConfirm,
    )
}

@Composable
private fun AlertDialogImpl(
    modifier: Modifier,
    icon: ImageVector?,
    title: String,
    description: String,
    confirmButtonLabel: String,
    cancelButtonLabel: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        icon =
            icon?.let {
                {
                    Icon(imageVector = it, contentDescription = "Alert Icon")
                }
            },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = description)
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirm,
            ) {
                Text(text = confirmButtonLabel)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onConfirm,
            ) {
                Text(text = cancelButtonLabel)
            }
        },
        properties =
            DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = true,
            ),
    )
}
