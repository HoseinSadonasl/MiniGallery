package com.hotaku.media_datasource.utils

import android.net.Uri
import android.provider.MediaStore

internal object MediaQueryUtils {
    val MediaStoreFileUri: Uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)

    val MediaProjection =
        arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.RELATIVE_PATH,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.DURATION,
            MediaStore.Files.FileColumns.DATE_TAKEN,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.IS_TRASHED,
            MediaStore.Files.FileColumns.IS_FAVORITE,
            MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
        )

    const val SORT_MEDIA_BY_DATE_ADDED = MediaStore.Files.FileColumns.DATE_ADDED + " DESC"

    fun String.getMediaUri(): Uri {
        return if (contains("image")) {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }
    }
}
