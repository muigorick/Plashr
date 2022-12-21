package com.muigorick.plashr.ui.activities.topics

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.muigorick.plashr.dataModels.topics.Topic
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class SingleTopicActivityViewModel @Inject constructor(private val topicPhotosRepository: TopicPhotosRepository) :
    ViewModel() {
    val topic = MutableLiveData<Topic>()
    val topicId = MutableLiveData<String>()

    val photos =
        topicId.switchMap { id ->
            topicPhotosRepository.getTopicPhotos(topicID = id)
                .cachedIn(viewModelScope)
        }

    fun setTopicID(topicID: String) {
        this.topicId.value = topicID
    }

    fun setTopic(topic: Topic) {
        this.topic.value = topic
    }
}