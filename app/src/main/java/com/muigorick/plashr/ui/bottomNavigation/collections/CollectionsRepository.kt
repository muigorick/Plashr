package com.muigorick.plashr.ui.bottomNavigation.collections

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.muigorick.plashr.api.CollectionsService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectionsRepository @Inject constructor(private val collectionsService: CollectionsService) {
    fun getCollections() =
        Pager(
            config = PagingConfig(
                pageSize = 30,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CollectionPagingSource(collectionsService = collectionsService)
            }
        ).liveData
}