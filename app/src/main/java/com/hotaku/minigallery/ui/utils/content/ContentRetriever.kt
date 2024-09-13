package com.hotaku.minigallery.ui.utils.content

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

val projection = arrayOf(
    MediaStore.Images.Media._ID,
    MediaStore.Images.Media.DISPLAY_NAME,
    MediaStore.Images.Media.ALBUM,
    MediaStore.Images.Media.DATE_ADDED,
    MediaStore.Images.Media.DATE_TAKEN,
    MediaStore.Images.Media.DATE_MODIFIED,
)

val orderByDate = "${MediaStore.Images.Media.DATE_ADDED} DESC"

suspend fun Context.imagesRetriever(order: String? = null): List<Image> = suspendCoroutine { continuation ->
    contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection, null, emptyArray(), order
    )?.use { curser ->
        val imageId = curser.getColumnIndex(MediaStore.Images.Media._ID)
        val imageName = curser.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
        val imageAlbum = curser.getColumnIndex(MediaStore.Images.Media.ALBUM)
        val imageDateAdded = curser.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
        val imageDateTaken = curser.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)
        val imageDateModified = curser.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)

        val images = mutableListOf<Image>()

        while (curser.moveToNext()) {
            images.add(
                Image(
                    imageId = curser.getLong(imageId),
                    imageName = curser.getString(imageName),
                    imageAlbum = curser.getString(imageAlbum),
                    imageDateAdded = curser.getString(imageDateAdded),
                    imageDateTaken = curser.getString(imageDateTaken),
                    imageDateModified = curser.getString(imageDateModified),
                    uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, curser.getLong(imageId))
                )
            )
        }
        continuation.resume(images)
    }
}

data class Image(
    val imageId: Long?,
    val imageName: String?,
    val imageAlbum: String?,
    val imageDateAdded: String?,
    val imageDateTaken: String?,
    val imageDateModified: String?,
    val uri: Uri,
)