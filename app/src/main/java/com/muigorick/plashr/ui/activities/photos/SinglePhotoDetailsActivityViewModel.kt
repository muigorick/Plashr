package com.muigorick.plashr.ui.activities.photos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muigorick.plashr.dataModels.photos.Photo

class SinglePhotoDetailsActivityViewModel : ViewModel() {

    private val _photoId = MutableLiveData<String>()
    val photoId: MutableLiveData<String>
        get() = _photoId

    private val _photoURL = MutableLiveData<String>()
    val photoURL: MutableLiveData<String>
        get() = _photoURL

    private val _photoDimensions = MutableLiveData<String>()
    val photoDimensions: MutableLiveData<String>
        get() = _photoDimensions

    private val _photoDescription = MutableLiveData<String>()
    val photoDescription: MutableLiveData<String>
        get() = _photoDescription

    private val _photoOwnerFullName = MutableLiveData<String>()
    val photoOwnerFullName: MutableLiveData<String>
        get() = _photoOwnerFullName

    private val _photoOwnerUsername = MutableLiveData<String>()
    val photoOwnerUsername: MutableLiveData<String>
        get() = _photoOwnerUsername

    private val _photoOwnerProfilePic = MutableLiveData<String>()
    val photoOwnerProfilePic: MutableLiveData<String>
        get() = _photoOwnerProfilePic

    private val _photoDateCreated = MutableLiveData<String>()
    val photoDateCreated: MutableLiveData<String>
        get() = _photoDateCreated

    private val _photoLikeCount = MutableLiveData<Int>()
    val photoLikeCount: MutableLiveData<Int>
        get() = _photoLikeCount

    private val _photoLikedByUser = MutableLiveData<Boolean>()
    val photoLikedByUser: MutableLiveData<Boolean>
        get() = _photoLikedByUser

    private val _photoCameraMake = MutableLiveData<String>()
    val photoCameraMake: MutableLiveData<String>
        get() = _photoCameraMake

    private val _photoCameraModel = MutableLiveData<String>()
    val photoCameraModel: MutableLiveData<String>
        get() = _photoCameraModel

    private val _photoCameraExposure = MutableLiveData<String>()
    val photoCameraExposure: MutableLiveData<String>
        get() = _photoCameraExposure

    private val _photoCameraAperture = MutableLiveData<String>()
    val photoCameraAperture: MutableLiveData<String>
        get() = _photoCameraAperture

    private val _photoCameraFocalLength = MutableLiveData<String>()
    val photoCameraFocalLength: MutableLiveData<String>
        get() = _photoCameraFocalLength

    private val _photoCameraISO = MutableLiveData<Int>()
    val photoCameraISO: MutableLiveData<Int>
        get() = _photoCameraISO

    private val _photoLocation = MutableLiveData<String>()
    val photoLocation: MutableLiveData<String>
        get() = _photoLocation

    private val _photoLoaded = MutableLiveData(false)
    val photoLoaded: MutableLiveData<Boolean>
        get() = _photoLoaded

    private val _photo = MutableLiveData<Photo>()
    val photo: MutableLiveData<Photo>
        get() = _photo

    fun setPhotoDetails(
        photoId: String?,
        photoURL: String?,
        photoDimensions: String?,
        photoDescription: String?,
        photoOwnerFullName: String?,
        photoOwnerUsername: String?,
        photoOwnerProfilePic: String?,
        photoDateCreated: String?,
        photoLikeCount: Int?,
        photoLikedByUser: Boolean?,
        photoCameraMake: String?,
        photoCameraModel: String?,
        photoCameraExposure: String?,
        photoCameraAperture: String?,
        photoCameraFocalLength: String?,
        photoCameraISO: Int?,
        photoLocation: String?,
    ) {
        this.photoURL.value = photoURL
        this.photoId.value = photoId
        this.photoDimensions.value = photoDimensions
        this.photoDescription.value = photoDescription
        this.photoOwnerFullName.value = photoOwnerFullName
        this.photoOwnerUsername.value = photoOwnerUsername
        this.photoOwnerProfilePic.value = photoOwnerProfilePic
        this.photoDateCreated.value = photoDateCreated
        this.photoLikeCount.value = photoLikeCount
        this.photoLikedByUser.value = photoLikedByUser
        this.photoCameraMake.value = photoCameraMake
        this.photoCameraModel.value = photoCameraModel
        this.photoCameraExposure.value = photoCameraExposure
        this.photoCameraAperture.value = photoCameraAperture
        this.photoCameraFocalLength.value = photoCameraFocalLength
        this.photoCameraISO.value = photoCameraISO
        this.photoLocation.value = photoLocation
    }
}