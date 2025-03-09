package com.hotaku.ui

import android.app.Activity.RESULT_OK
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore.createTrashRequest
import android.provider.MediaStore.createWriteRequest
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
import com.hotaku.ui.models.MediaUi

@Composable
fun Uri.asThumbnailImageBitmap(size: Size = Size(320, 320)): ImageBitmap {
    val context = LocalContext.current
    val thumbnail =
        context.contentResolver.loadThumbnail(this, size, null)
    return thumbnail.asImageBitmap()
}

@Composable
fun rememberLauncherForStartIntentSenderForResult(block: () -> Unit) =
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { if (it.resultCode == RESULT_OK) block() },
    )

fun String.trashMediaItemByUri(
    context: Context,
    trashLauncher: ActivityResultLauncher<IntentSenderRequest>,
) {
    listOf(this).trashMediaByUri(
        context = context,
        trashLauncher = trashLauncher,
    )
}

fun List<String>.trashMediaByUri(
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

    val intentSenderRequest = deleteRequest.createRequest()
    trashLauncher.launch(intentSenderRequest)
}

fun MediaUi.sendShareIntent(context: Context) {
    val sendIntent =
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uriString.toUri())
            type = "$mimeType/*"
        }
    val intentToShare = Intent.createChooser(sendIntent, displayName)
    context.startActivity(intentToShare)
}

fun MediaUi.sendPlayIntent(context: Context) {
    val sendIntent = Intent(Intent.ACTION_VIEW, uriString.toUri())
    context.startActivity(sendIntent)
}

fun String.writeMediaRequest(
    context: Context,
    trashLauncher: ActivityResultLauncher<IntentSenderRequest>,
) {
    listOf(this).writeGroupOfMediaRequest(
        context = context,
        trashLauncher = trashLauncher,
    )
}

fun List<String>.writeGroupOfMediaRequest(
    context: Context,
    trashLauncher: ActivityResultLauncher<IntentSenderRequest>,
) {
    val resolver = context.contentResolver

    val writeRequest =
        createWriteRequest(
            resolver,
            this.map { it.toUri() },
        )

    val intentSenderRequest = writeRequest.createRequest()
    trashLauncher.launch(intentSenderRequest)
}

private fun PendingIntent.createRequest() =
    IntentSenderRequest.Builder(this.intentSender)
        .setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, 0)
        .build()
