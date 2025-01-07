package com.hotaku.media_datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hotaku.data.model.MediaData
import com.hotaku.database.dao.MediaDao
import com.hotaku.media_datasource.mapper.MapMediaEntityAsMediaData

internal class MediaPagingSource(
    private val mapMediaEntityAsMediaData: MapMediaEntityAsMediaData,
    private val mediaDao: MediaDao,
    private val mimeType: String?,
    private val query: String?,
) : PagingSource<Int, MediaData>() {
    override fun getRefreshKey(state: PagingState<Int, MediaData>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaData> {
        val page = params.key ?: START_PAGE_KEY

        return try {
            val media =
                mediaDao.getAll(
                    query = query,
                    mimeType = mimeType,
                    limit = params.loadSize,
                    offset = page * params.loadSize,
                ).map { mapMediaEntityAsMediaData.map(it) }
            LoadResult.Page(
                data = media,
                prevKey = page.minus(1).takeIf { page > 0 },
                nextKey = page.plus(1).takeIf { media.isNotEmpty() },
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        private const val START_PAGE_KEY = 0
    }
}
