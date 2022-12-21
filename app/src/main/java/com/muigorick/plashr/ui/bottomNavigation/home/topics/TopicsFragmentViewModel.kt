package com.muigorick.plashr.ui.bottomNavigation.home.topics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.muigorick.plashr.ui.bottomNavigation.topics.TopicsFragmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopicsFragmentViewModel @Inject constructor
    (topicsFragmentRepository: TopicsFragmentRepository) :
    ViewModel() {
    val topics = topicsFragmentRepository
        .getTopics()
        .cachedIn(viewModelScope)

}