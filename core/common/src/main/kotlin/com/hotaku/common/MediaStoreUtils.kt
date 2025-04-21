package com.hotaku.common

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.MediaStore.createDeleteRequest
import android.provider.MediaStore.createTrashRequest
import android.provider.MediaStore.createWriteRequest
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.net.toUri

fun String.trashMediaRequest(
    context: Context,
    trashLauncher: ActivityResultLauncher<IntentSenderRequest>,
    trash: Boolean = true,
) {
    listOf(this).trashMediaRequest(
        context = context,
        trashLauncher = trashLauncher,
        trash = trash,
    )
}

fun List<String>.trashGroupOfMediaRequest(
    context: Context,
    trashLauncher: ActivityResultLauncher<IntentSenderRequest>,
    trash: Boolean = true,
) = trashMediaRequest(
    context = context,
    trashLauncher = trashLauncher,
    trash = trash,
)

private fun List<String>.trashMediaRequest(
    context: Context,
    trashLauncher: ActivityResultLauncher<IntentSenderRequest>,
    trash: Boolean,
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
    deleteLauncher: ActivityResultLauncher<IntentSenderRequest>,
) {
    listOf(this).deleteMediaRequest(
        context = context,
        deleteLauncher = deleteLauncher,
    )
}

fun List<String>.deleteGroupOfMediaRequest(
    context: Context,
    deleteLauncher: ActivityResultLauncher<IntentSenderRequest>,
) {
    deleteMediaRequest(
        context = context,
        deleteLauncher = deleteLauncher,
    )
}

private fun List<String>.deleteMediaRequest(
    context: Context,
    deleteLauncher: ActivityResultLauncher<IntentSenderRequest>,
) {
    val resolver = context.contentResolver
    val deleteRequest =
        createDeleteRequest(
            resolver,
            this.map { it.toUri() },
        )

    val intentSenderRequest = deleteRequest.createRequest()
    deleteLauncher.launch(intentSenderRequest)
}

private fun PendingIntent.createRequest() =
    IntentSenderRequest.Builder(this.intentSender)
        .setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, 0)
        .build()
