package com.hotaku.media.utils

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore.createTrashRequest
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.hotaku.media.model.MediaUi

@Composable
internal fun Uri.asThumbnailImageBitmap(size: Size = Size(320, 320)): ImageBitmap {
    val context = LocalContext.current
    val thumbnail =
        context.contentResolver.loadThumbnail(this, size, null)
    return thumbnail.asImageBitmap()
}

@Composable
internal fun rememberTrashLauncherForResult(block: () -> Unit) =
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { if (it.resultCode == RESULT_OK) block() },
    )

internal fun String.trashMediaItemByUri(
    context: Context,
    trashLauncher: ActivityResultLauncher<IntentSenderRequest>,
) {
    listOf(this).trashMediaByUri(
        context = context,
        trashLauncher = trashLauncher,
    )
}

internal fun List<String>.trashMediaByUri(
    context: Context,
    trashLauncher: ActivityResultLauncher<IntentSenderRequest>,
) {
    val resolver = context.contentResolver

    val deleteRequest =
        createTrashRequest(
            resolver,
            this.map { it.toUri() },
            true,
        )

    val intentSenderRequest =
        IntentSenderRequest.Builder(deleteRequest.intentSender)
            .setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, 0)
            .build()

    trashLauncher.launch(intentSenderRequest)
}

internal fun MediaUi.sendIntent(
    context: Context,
    intentAction: String,
) {
    val sendIntent =
        Intent().apply {
            action = intentAction
            putExtra(Intent.EXTRA_STREAM, Uri.parse(uriString))
            type = "$mimeType/*"
        }
    val intentToShare = Intent.createChooser(sendIntent, displayName)
    context.startActivity(intentToShare)
}
