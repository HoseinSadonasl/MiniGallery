package com.hotaku.ui

import android.content.Context
import android.net.Uri
import android.os.CancellationSignal
import android.util.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.job
import kotlinx.coroutines.withContext

suspend fun Uri.asThumbnailImageBitmap(
    context: Context,
    size: Size = Size(320, 320),
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
): Result<ImageBitmap> =
    runCatching {
        val signal = CancellationSignal()
        withContext(dispatcher) {
            coroutineContext.job.invokeOnCompletion {
                signal.cancel()
            }
            context.contentResolver.loadThumbnail(
                this@asThumbnailImageBitmap,
                size,
                signal,
            )
                .asImageBitmap()
        }
    }.onFailure { it.printStackTrace() }
