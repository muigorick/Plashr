package com.muigorick.plashr.ui.bottomNavigation.topics

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.muigorick.plashr.api.TopicsService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopicsFragmentRepository @Inject constructor(private val topicsService: TopicsService) {

    fun getTopics() = Pager(
        config = PagingConfig(
            pageSize = 30,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            TopicsFragmentPagingSource(topicsService = topicsService)
        }
    ).liveData

}