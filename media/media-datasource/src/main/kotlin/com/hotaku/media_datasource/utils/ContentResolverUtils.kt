package com.hotaku.media_datasource.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
import com.hotaku.media_datasource.models.MediaDto
import com.hotaku.media_datasource.utils.MediaQueryUtils.getMediaUri

internal fun ContentResolver.queryMediaFromContentProvider(
    uri: Uri,
    projection: Array<String>? = null,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    sortOrder: String? = MediaQueryUtils.SORT_MEDIA_BY_DATE_ADDED,
): Result<List<MediaDto>> =
    runCatching {
        query(
            uri,
            projection,
            selection,
            selectionArgs,
            sortOrder,
        )?.use { it.processCursor() }.orEmpty()
    }

private fun Cursor.processCursor(): List<MediaDto> {
    val mediaList = mutableListOf<MediaDto>()

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
                getString(mimeType).getMediaUri(),
                getLong(mediaId),
            )
        MediaDto(
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

internal fun ContentResolver.renameMedia(
    mediaUriString: String,
    name: String,
) = runCatching {
    val contentValues =
        ContentValues().apply {
            put(MediaStore.Files.FileColumns.DISPLAY_NAME, name)
        }
    update(mediaUriString.toUri(), contentValues, null) > 0
}
