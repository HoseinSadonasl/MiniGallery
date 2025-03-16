package com.hotaku.common

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.MediaStore.createTrashRequest
import android.provider.MediaStore.createWriteRequest
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.net.toUri

fun String.trashMediaRequest(
    context: Context,
    trashLauncher: ActivityResultLauncher<IntentSenderRequest>,
) {
    listOf(this).trashMediaRequest(
        context = context,
        trashLauncher = trashLauncher,
    )
}

fun List<String>.trashGroupOfMediaRequest(
    context: Context,
    trashLauncher: ActivityResultLauncher<IntentSenderRequest>,
) = trashMediaRequest(
    context = context,
    trashLauncher = trashLauncher,
)

private fun List<String>.trashMediaRequest(
    context: Context,
    trashLauncher: ActivityResultLauncher<IntentSenderRequest>,
    trash: Boolean = true,
) {
    val resolver = context.contentResolver

    val trashRequest =
        createTrashRequest(
            resolver,
            this.map { it.toUri() },
            trash,
        )

    val intentSenderRequest = trashRequest.createRequest()
    trashLauncher.launch(intentSenderRequest)
}

fun String.writeMediaRequest(
    context: Context,
    writeLauncher: ActivityResultLauncher<IntentSenderRequest>,
) {
    listOf(this).writeMediaRequest(
        context = context,
        writeLauncher = writeLauncher,
    )
}

fun String.writeGroupOfMediaRequest(
    context: Context,
    writeLauncher: ActivityResultLauncher<IntentSenderRequest>,
) = writeMediaRequest(
    context = context,
    writeLauncher = writeLauncher,
)

private fun List<String>.writeMediaRequest(
    context: Context,
    writeLauncher: ActivityResultLauncher<IntentSenderRequest>,
) {
    val resolver = context.contentResolver

    val writeRequest =
        createWriteRequest(
            resolver,
            this.map { it.toUri() },
        )

    val intentSenderRequest = writeRequest.createRequest()
    writeLauncher.launch(intentSenderRequest)
}

fun String.deleteMediaRequest(
    context: Context,
    trashLauncher: ActivityResultLauncher<IntentSenderRequest>,
    trash: Boolean,
) {
    listOf(this).deleteMediaRequest(
        context = context,
        trashLauncher = trashLauncher,
        trash = trash,
    )
}

fun List<String>.deleteGroupOfMediaRequest(
    context: Context,
    trashLauncher: ActivityResultLauncher<IntentSenderRequest>,
    trash: Boolean,
) {
    deleteMediaRequest(
        context = context,
        trashLauncher = trashLauncher,
        trash = trash,
    )
}

private fun List<String>.deleteMediaRequest(
    context: Context,
    trashLauncher: ActivityResultLauncher<IntentSenderRequest>,
    trash: Boolean,
) {
    val resolver = context.contentResolver
    val deleteRequest =
        createTrashRequest(
            resolver,
            this.map { it.toUri() },
            trash,
        )

    val intentSenderRequest = deleteRequest.createRequest()
    trashLauncher.launch(intentSenderRequest)
}

private fun PendingIntent.createRequest() =
    IntentSenderRequest.Builder(this.intentSender)
        .setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, 0)
        .build()
