package com.hotaku.ui

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import com.hotaku.ui.models.MediaUi

@Composable
fun rememberLauncherForStartIntentSenderForResult(block: () -> Unit) =
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { if (it.resultCode == RESULT_OK) block() },
    )

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
