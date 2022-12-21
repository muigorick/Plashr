package com.muigorick.plashr.ui.activities.collections

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.muigorick.plashr.api.CollectionsService
import com.muigorick.plashr.dataModels.photos.Photo
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE = 1

class CollectionPhotosPagingSource(
    private val collectionService: CollectionsService,
    private val collectionID: String
) : PagingSource<Int, Photo>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val pagePosition = params.key ?: STARTING_PAGE
        return try {
            val response = collectionService.getCollectionPhotos(
                id = collectionID,
                page =pagePosition,
                per_page = params.loadSize
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

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        TODO("Not yet implemented")
    }


}