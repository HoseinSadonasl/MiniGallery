package com.hotaku.media.utils

import android.net.Uri
import android.util.Size
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext

@Composable
fun Uri.asThumbnailImageBitmap(size: Size = Size(320, 320)): ImageBitmap {
    val context = LocalContext.current
    val thumbnail =
        context.contentResolver.loadThumbnail(this, size, null)
    return thumbnail.asImageBitmap()
}
