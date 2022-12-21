package com.muigorick.plashr.ui.activities.collections

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.muigorick.plashr.api.CollectionsService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectionPhotosRepository @Inject constructor(private val collectionService: CollectionsService) {

    fun getCollectionPhotos(collectionID: String) =
        Pager(
            config = PagingConfig(
                pageSize = 30,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CollectionPhotosPagingSource(
                    collectionService = collectionService,
                    collectionID = collectionID
                )
            }
        ).liveData
}