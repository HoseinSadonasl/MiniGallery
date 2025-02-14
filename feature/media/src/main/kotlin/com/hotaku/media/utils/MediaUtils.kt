package com.hotaku.media.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Size
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.hotaku.media.model.MediaUi

@Composable
internal fun Uri.asThumbnailImageBitmap(size: Size = Size(320, 320)): ImageBitmap {
    val context = LocalContext.current
    val thumbnail =
        context.contentResolver.loadThumbnail(this, size, null)
    return thumbnail.asImageBitmap()
}

internal fun MediaUi.shareMedia(context: Context) {
    val sendIntent =
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, Uri.parse(uriString))
            type = "$mimeType/*"
        }
    val intentToShare = Intent.createChooser(sendIntent, displayName)
    context.startActivity(intentToShare)
}
