package com.muigorick.plashr.actions

import android.content.Context
import android.content.Intent
import com.muigorick.plashr.R
import com.muigorick.plashr.actions.PhotoActions.OnPhotoActionsListener
import com.muigorick.plashr.dataModels.photos.Photo
import com.muigorick.plashr.network.AppModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Class to photo actions. It allows the user to share photos, add & remove photos to and from collections,
 * download, like and unlike photos.
 * @param context Context needed to carry out actions that require context.
 * @param listener Listener that triggers the [OnPhotoActionsListener] interface whenever selected actions
 *([sharePhoto]) succeed or fail.
 */
class PhotoActions(
    private val context: Context,
    private val photo: Photo,
    private val listener: OnPhotoActionsListener
) {

    private val dataService =
        AppModule.providePhotoDataService(retrofit = AppModule.provideRetrofit())

    /**
     * Image Sharing through intent.
     */
    fun sharePhoto() {
        val sendIntent = Intent()
        sendIntent.apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                context.getString(
                    R.string.share_photo_intent_message,
                    photo.user?.username,
                    photo.attributionUrl
                )
            )
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share image")
        context.startActivity(shareIntent)
    }

    /**
     * Adds a selected photo to collection.
     */
    fun addPhotoToCollection() {

    }

    /**
     * Remove a selected photo from collection.
     */
    fun removePhotoFromCollection() {

    }

    /**
     *  Download a selected photo.
     */
    fun downloadPhoto() {

    }

    /**
     * Like a selected photo.
     */
    fun likePhoto() {
        val likePhotoCall = dataService.likePhoto(photo.id!!)
        likePhotoCall.enqueue(object : Callback<Photo?> {
            override fun onResponse(call: Call<Photo?>, response: Response<Photo?>) {
                listener.onPhotoLikeSuccess()
            }

            override fun onFailure(call: Call<Photo?>, t: Throwable) {
                listener.onPhotoLikeFailure()
            }
        })
    }

    /**
     * Unlike a selected photo.
     */
    fun unlikePhoto() {
        val unlikePhotoCall = dataService.unlikePhoto(photo.id!!)
        unlikePhotoCall.enqueue(object : Callback<Photo?> {
            override fun onResponse(call: Call<Photo?>, response: Response<Photo?>) {
                listener.onPhotoUnlikeSuccess()
            }

            override fun onFailure(call: Call<Photo?>, t: Throwable) {
                listener.onPhotoUnlikeFailure()
            }
        })
    }

    /**
     * Photo actions listener interface.
     */
    interface OnPhotoActionsListener {
        fun onPhotoLikeSuccess()
        fun onPhotoLikeFailure()
        fun onPhotoUnlikeSuccess()
        fun onPhotoUnlikeFailure()
        fun onPhotoDownloadSuccess()
        fun onPhotoDownloadFailure()
    }
}