package com.hotaku.media_datasource.content_provider

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.hotaku.common.Logger
import com.hotaku.common.Logger.debugErrorLog
import com.hotaku.media_datasource.content_provider.MediaQueryUtils.getMediaUri
import com.hotaku.media_datasource.models.MediaDto

internal fun ContentResolver.queryMediaFromContentProvider(
    uri: Uri,
    projection: Array<String>? = null,
): List<MediaDto> =
    runCatching {
        query(
            uri,
            projection,
            bundleOf(
                MediaStore.QUERY_ARG_MATCH_TRASHED to MediaStore.MATCH_INCLUDE,
                MediaStore.QUERY_ARG_MATCH_FAVORITE to MediaStore.MATCH_INCLUDE,
            ),
            null,
        ).use { it?.processCursor() ?: emptyList() }
    }.getOrElse {
        it.debugErrorLog(kFun = this::queryMediaFromContentProvider)
        emptyList()
    }

private fun Cursor.processCursor(): List<MediaDto> =
    runCatching {
        val mediaList = mutableListOf<MediaDto>()

        val mediaId = getColumnIndex(MediaStore.Files.FileColumns._ID)
        val displayName = getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
        val mimeType = getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)
        val duration = getColumnIndex(MediaStore.Files.FileColumns.DURATION)
        val dateAdded = getColumnIndex(MediaStore.Files.FileColumns.DATE_TAKEN)
        val dateModified = getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)
        val size = getColumnIndex(MediaStore.Files.FileColumns.SIZE)
        val isTrash = getColumnIndex(MediaStore.Files.FileColumns.IS_TRASHED)
        val isFavorite = getColumnIndex(MediaStore.Files.FileColumns.IS_FAVORITE)
        val bucketDisplayName = getColumnIndex(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)

        while (moveToNext()) {
            val uriString =
                ContentUris.withAppendedId(
                    getString(mimeType).getMediaUri(),
                    getLong(mediaId),
                )

            if (getInt(isTrash) == 1) {
                Logger.debugWarningLog(kClass = this::class, message = "Media is in trash")
            }
            MediaDto(
                mediaId = getLong(mediaId),
                uriString = uriString.toString(),
                displayName = getString(displayName),
                mimeType = getString(mimeType),
                duration = getInt(duration).toString(),
                dateAdded = getLong(dateAdded),
                dateModified = getLong(dateModified),
                size = getLong(size),
                isTrash = getInt(isTrash) == 1,
                isFavorite = getInt(isFavorite) == 1,
                bucketDisplayName = getString(bucketDisplayName),
            ).also { mediaList.add(it) }
        }
        mediaList
    }.getOrElse {
        it.debugErrorLog(kFun = this::processCursor)
        emptyList()
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
}.getOrElse {
    it.debugErrorLog(kClass = this::class)
    false
}
