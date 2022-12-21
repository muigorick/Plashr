package com.muigorick.plashr.ui.bottomNavigation.home

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.muigorick.plashr.api.PhotoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeFragmentPhotosRepository @Inject constructor(private val photoService: PhotoService) {

    fun getPhotos() =
        Pager(
            config = PagingConfig(
                pageSize = 30,
                initialLoadSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                HomeFragmentPhotosPagingSource(
                    photoService = photoService
                )
            }
        ).flow
}