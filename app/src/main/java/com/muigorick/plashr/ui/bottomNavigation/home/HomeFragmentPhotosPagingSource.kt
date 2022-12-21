package com.muigorick.plashr.ui.bottomNavigation.home

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.muigorick.plashr.api.PhotoService
import com.muigorick.plashr.dataModels.photos.Photo

import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE = 1

class HomeFragmentPhotosPagingSource(
    private val photoService: PhotoService,
) : PagingSource<Int, Photo>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {

        val pagePosition = params.key ?: STARTING_PAGE

        return try {
            val response = photoService.getPhotos(
                page = pagePosition,
                per_page = params.loadSize,
                order_by = ""
            )

            LoadResult.Page(
                data = response,
                prevKey = if (pagePosition == STARTING_PAGE) null else pagePosition - 1,
                nextKey = if (response.isEmpty()) null else pagePosition + 1
            )

        } catch (exception: IOException) {
            Log.i("Error: ", "IOException: $exception")
            LoadResult.Error(exception)

        } catch (exception: HttpException) {
            Log.i("Error: ", "HttpException: $exception")
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}