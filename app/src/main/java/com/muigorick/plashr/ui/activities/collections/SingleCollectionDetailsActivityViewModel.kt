package com.muigorick.plashr.ui.activities.collections

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.muigorick.plashr.dataModels.collections.Collection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SingleCollectionDetailsActivityViewModel @Inject constructor(private val collectionPhotosRepository: CollectionPhotosRepository) :
    ViewModel() {
    private val collection = MutableLiveData<Collection>()
    private val collectionId = MutableLiveData<String>()
    private val collectionTitle = MutableLiveData<String>()
    private val collectionDescription = MutableLiveData<String>()
    private val collectionPublishedDate = MutableLiveData<String>()
    private val collectionTotalPhotos = MutableLiveData<Int>()
    private val collectionIsPrivate = MutableLiveData<Boolean>()
    private val collectionCoverPhoto = MutableLiveData<Collection.CoverPhoto>()
    private val collectionUser = MutableLiveData<Collection.User>()

    val collectionPhotos =
        collectionId.switchMap { id ->
            collectionPhotosRepository.getCollectionPhotos(collectionID = id)
                .cachedIn(viewModelScope)
        }

    fun setCollectionDetails(
        collectionId: String?,
        collectionTitle: String?,
        collectionDescription: String?,
        collectionPublishedDate: String?,
        collectionTotalPhotos: Int?,
        collectionIsPrivate: Boolean?,
        collectionCoverPhoto: Collection.CoverPhoto?,
        collectionUser: Collection.User?
    ) {
        this.collectionId.value = collectionId
        this.collectionTitle.value = collectionTitle
        this.collectionDescription.value = collectionDescription
        this.collectionPublishedDate.value = collectionPublishedDate
        this.collectionTotalPhotos.value = collectionTotalPhotos
        this.collectionIsPrivate.value = collectionIsPrivate
        this.collectionCoverPhoto.value = collectionCoverPhoto
        this.collectionUser.value = collectionUser
    }

    fun setCollection(collection: Collection) {
        this.collection.value = collection
    }

    fun getCollection(): MutableLiveData<Collection> {
        return collection
    }

    fun getCollectionID(): LiveData<String> {
        return collectionId
    }

    fun getCollectionTitle(): LiveData<String> {
        return collectionTitle
    }

    fun getCollectionDescription(): LiveData<String> {
        return collectionDescription
    }

    fun getCollectionPublishedDate(): LiveData<String> {
        return collectionPublishedDate
    }

    fun getCollectionTotalPhotos(): LiveData<Int> {
        return collectionTotalPhotos
    }

    fun getCollectionIsPrivate(): LiveData<Boolean> {
        return collectionIsPrivate
    }

    fun getCollectionCoverPhoto(): LiveData<Collection.CoverPhoto> {
        return collectionCoverPhoto
    }

    fun getCollectionUser(): LiveData<Collection.User> {
        return collectionUser
    }
}