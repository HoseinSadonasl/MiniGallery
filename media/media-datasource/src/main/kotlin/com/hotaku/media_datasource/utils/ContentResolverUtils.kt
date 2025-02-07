package com.hotaku.media_datasource.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.hotaku.data.model.MediaData

fun ContentResolver.queryMedia(
    uri: Uri,
    projection: Array<String>? = null,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    sortOrder: String? = MediaQueries.SORT_MEDIA_BY_DATE_ADDED,
): Result<List<MediaData>> =
    runCatching {
        query(
            uri,
            projection,
            selection,
            selectionArgs,
            sortOrder,
        )?.use { it.processCursor() }.orEmpty()
    }

fun Cursor.processCursor(): List<MediaData> {
    val mediaList = mutableListOf<MediaData>()

    val mediaId = getColumnIndex(MediaStore.Files.FileColumns._ID)

    val displayName = getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
    val mimeType = getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)
    val duration = getColumnIndex(MediaStore.Files.FileColumns.DURATION)
    val dateAdded = getColumnIndex(MediaStore.Files.FileColumns.DATE_TAKEN)
    val dateModified = getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)
    val size = getColumnIndex(MediaStore.Files.FileColumns.SIZE)
    val bucketDisplayName = getColumnIndex(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)

    while (moveToNext()) {
        val uriString =
            ContentUris.withAppendedId(
                MediaQueries.MediaStoreFileUri,
                getLong(mediaId),
            )
        MediaData(
            mediaId = getLong(mediaId),
            uriString = uriString.toString(),
            displayName = getString(displayName),
            mimeType = getString(mimeType),
            duration = getInt(duration).toString(),
            dateAdded = getLong(dateAdded),
            dateModified = getLong(dateModified),
            size = getLong(size),
            bucketDisplayName = getString(bucketDisplayName),
        ).also { mediaList.add(it) }
    }
    return mediaList
}
