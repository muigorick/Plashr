package com.muigorick.plashr.ui.bottomNavigation.home.editorialPhotos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.muigorick.plashr.ui.bottomNavigation.home.HomeFragmentPhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class EditorialPhotosFragmentViewModel @Inject constructor(
    homeFragmentPhotosRepository: HomeFragmentPhotosRepository
) :
    ViewModel() {
    val photos = homeFragmentPhotosRepository
        .getPhotos()
        .cachedIn(viewModelScope)
}