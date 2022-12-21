package com.muigorick.plashr.ui.bottomNavigation.topics

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.muigorick.plashr.api.TopicsService
import com.muigorick.plashr.dataModels.topics.Topic
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE = 1

class TopicsFragmentPagingSource(
    private val topicsService: TopicsService,
) : PagingSource<Int, Topic>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Topic> {

        val pagePosition = params.key ?: STARTING_PAGE

        return try {
            val response = topicsService.getTopics(
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
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Topic>): Int? {
        TODO("Not yet implemented")
    }
}