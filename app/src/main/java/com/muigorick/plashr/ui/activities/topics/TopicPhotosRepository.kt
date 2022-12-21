package com.muigorick.plashr.ui.activities.topics

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.muigorick.plashr.api.TopicsService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopicPhotosRepository @Inject constructor(private val topicsService: TopicsService) {

    fun getTopicPhotos(topicID: String) =
        Pager(
            config = PagingConfig(
                pageSize = 30,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                TopicsPhotosPagingSource(
                    topicID = topicID,
                    topicService = topicsService
                )
            }
        ).liveData
}