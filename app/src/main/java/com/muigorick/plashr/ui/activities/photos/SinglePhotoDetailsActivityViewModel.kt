package com.muigorick.plashr.ui.activities.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muigorick.plashr.dataModels.photos.Photo

class SinglePhotoDetailsActivityViewModel : ViewModel() {

    private val photoId = MutableLiveData<String>()
    private val photoURL = MutableLiveData<String>()
    private val photoDimensions = MutableLiveData<String>()
    private val photoDescription = MutableLiveData<String>()
    private val photoOwnerFullName = MutableLiveData<String>()
    private val photoOwnerUsername = MutableLiveData<String>()
    private val photoOwnerProfilePic = MutableLiveData<String>()
    private val photoDateCreated = MutableLiveData<String>()
    private val photoLikeCount = MutableLiveData<Int>()
    private val photoLikedByUser = MutableLiveData<Boolean>()
    private val photoCameraMake = MutableLiveData<String>()
    private val photoCameraModel = MutableLiveData<String>()
    private val photoCameraExposure = MutableLiveData<String>()
    private val photoCameraAperture = MutableLiveData<String>()
    private val photoCameraFocalLength = MutableLiveData<String>()
    private val photoCameraISO = MutableLiveData<Int>()
    private val photoLocation = MutableLiveData<String>()
    private val shareImage = MutableLiveData<Boolean>()
    private val photo = MutableLiveData<Photo>()
    private val photoLoaded = MutableLiveData<Boolean>(false)

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

    fun getPhotoURL(): LiveData<String> {
        return photoURL
    }

    fun getPhotoDimensions(): LiveData<String> {
        return photoDimensions
    }

    fun getPhotoID(): LiveData<String> {
        return photoId
    }

    fun setPhotoID(photoId: String?) {
        this.photoId.value = photoId
    }

    fun getPhotoDescription(): LiveData<String> {
        return photoDescription
    }

    fun getPhotoOwnerFullName(): LiveData<String> {
        return photoOwnerFullName
    }

    fun getPhotoOwnerUsername(): LiveData<String> {
        return photoOwnerUsername
    }

    fun getPhotoOwnerProfilePic(): LiveData<String> {
        return photoOwnerProfilePic
    }

    fun getPhotoDateCreated(): LiveData<String> {
        return photoDateCreated
    }

    fun getPhotoLikeCount(): LiveData<Int> {
        return photoLikeCount
    }

    fun getPhotoLikedByUser(): LiveData<Boolean> {
        return photoLikedByUser
    }

    fun getPhotoCameraMake(): LiveData<String> {
        return photoCameraMake
    }

    fun getPhotoCameraModel(): LiveData<String> {
        return photoCameraModel
    }

    fun getPhotoCameraExposure(): LiveData<String> {
        return photoCameraExposure
    }

    fun getPhotoCameraAperture(): LiveData<String> {
        return photoCameraAperture
    }

    fun getPhotoCameraFocalLength(): LiveData<String> {
        return photoCameraFocalLength
    }

    fun getPhotoCameraISO(): LiveData<Int> {
        return photoCameraISO
    }

    fun getPhotoLocation(): LiveData<String> {
        return photoLocation
    }

    fun getShareImage(): LiveData<Boolean> {
        return shareImage
    }

    fun setShareImage(shareImage: Boolean) {
        this.shareImage.value = shareImage
    }

    fun setPhoto(photo: Photo) {
        this.photo.value = photo
    }

    fun getPhoto(): MutableLiveData<Photo> {
        return photo
    }

    fun setPhotoLoaded(loaded: Boolean) {
        this.photoLoaded.value = loaded
    }

    fun getPhotoLoaded(): MutableLiveData<Boolean> {
        return photoLoaded
    }

}