package com.muigorick.plashr.actions

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import androidx.core.text.HtmlCompat
import com.muigorick.plashr.R
import com.muigorick.plashr.dataModels.photos.Photo

class PhotoActions(private val context: Context,private val photo: Photo) {

    /**
     * Image Sharing through intent.
     */
     fun sharePhoto() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.type = "text/plain"
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            context.getString(
                R.string.share_photo_intent_message,
                HtmlCompat.fromHtml(
                    (context.getString(
                        R.string.user_share_name_link,
                        photo.user?.username,
                        photo.user?.username
                    )), HtmlCompat.FROM_HTML_MODE_LEGACY
                ), photo.links?.html
            )
        )
        val shareIntent = Intent.createChooser(sendIntent, "Share image")
        context.startActivity(shareIntent)
    }

    /**
     * Adds a selected photo to collection.
     */
    fun addPhotoToCollection(){

    }

    /**
     * Remove a selected photo from collection.
     */
    fun removePhotoFromCollection(){

    }

   /**
     *  Download a selected photo.
     */
    fun downloadPhoto(download_manager: DownloadManager, context: Context) {


    }

    /**
     * Like a selected photo.
     */
    fun likePhoto(){

    }

    /**
     * Dislike a selected photo.
     */
    fun dislikePhoto(){

    }
}