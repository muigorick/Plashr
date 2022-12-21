package com.muigorick.plashr.ui.bottomNavigation.collections

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.muigorick.plashr.api.CollectionsService
import com.muigorick.plashr.dataModels.collections.Collection
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE = 1

class CollectionPagingSource(
    private val collectionsService: CollectionsService,
) : PagingSource<Int, Collection>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Collection> {

        val pagePosition = params.key ?: STARTING_PAGE

        return try {
            val response = collectionsService.getCollections(
                page = pagePosition,
                per_page = params.loadSize,
            )

            LoadResult.Page(
                data = response,
                prevKey = if (pagePosition == STARTING_PAGE) null else pagePosition - 1,
                nextKey = if (response.isEmpty()) null else pagePosition + 1
            )

        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Collection>): Int? {
        TODO("Not yet implemented")
    }
}