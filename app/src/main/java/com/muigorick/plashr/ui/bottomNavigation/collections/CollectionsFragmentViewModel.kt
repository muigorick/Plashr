package com.muigorick.plashr.ui.bottomNavigation.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectionsFragmentViewModel @Inject constructor(
    collectionsRepository: CollectionsRepository
) : ViewModel() {
    val collections = collectionsRepository
        .getCollections()
        .cachedIn(viewModelScope)
}