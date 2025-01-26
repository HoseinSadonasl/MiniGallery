package com.hotaku.media_datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hotaku.data.model.MediaData
import com.hotaku.database.dao.MediaDao
import com.hotaku.media_datasource.mapper.MapMediaEntityAsMediaData
import java.io.IOException
import java.net.UnknownHostException

internal class MediaPagingSource(
    private val mapMediaEntityAsMediaData: MapMediaEntityAsMediaData,
    private val mediaDao: MediaDao,
    private val mimeType: String,
    private val query: String,
) : PagingSource<Int, MediaData>() {
    override fun getRefreshKey(state: PagingState<Int, MediaData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let { page ->
                page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaData> {
        val page = params.key ?: START_PAGE_KEY

        val media =
            mediaDao.getAll(
                query = query.takeIf { it.isNotEmpty() },
                mimeType = mimeType.takeIf { it.isNotEmpty() },
                limit = params.loadSize,
                offset = page * params.loadSize,
            ).map { mapMediaEntityAsMediaData.map(it) }

        return try {
            LoadResult.Page(
                data = media,
                prevKey = null,
                nextKey = page.plus(1).takeIf { media.isNotEmpty() },
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: UnknownHostException) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        private const val START_PAGE_KEY = 0
    }
}
