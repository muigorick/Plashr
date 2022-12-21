package com.muigorick.plashr.dataModels.photos

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.muigorick.plashr.dataModels.collections.Collection
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Photo(
    @SerializedName("id") val id: String?,
    @SerializedName("created_at") val created_at: String?,
    @SerializedName("width") val width: Int?,
    @SerializedName("height") val height: Int?,
    @SerializedName("color") val color: String?,
    @SerializedName("likes") val likes: Int?,
    @SerializedName("location") val location: PhotoLocation?,
    @SerializedName("exif") val exif: PhotoExif?,
    @SerializedName("liked_by_user") val likedByUser: Boolean?,
    @SerializedName("description") val description: String?,
    @SerializedName("user") val user: User?,
    @SerializedName("urls") val urls: Collection.CoverPhoto.Urls?,
    @SerializedName("links") val links: Links?,
    @SerializedName("tags") val tags: List<PhotoTags>?
) : Parcelable {

    //Our attribution URL as required by the unsplash guidelines
    val attributionUrl get() = "https://unsplash.com/photos/$id?utm_source=Plashr&utm_medium=referral"

    /**
     * Converts and Returns a ColorDrawable from the color string we get from the API
     */
    val photoColor: ColorDrawable
        get():ColorDrawable {
            val photoColor = Color.parseColor(color)
            return ColorDrawable(photoColor)
        }

    val dateUploaded: String
        get() {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formatTo = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

            return formatTo.format(formatter.parse(created_at!!)!!)
        }

    @Parcelize
    data class User(
        @SerializedName("id") val id: String,
        @SerializedName("username") val username: String,
        @SerializedName("name") val name: String,
        @SerializedName("portfolio_url") val portfolioURL: String?,
        @SerializedName("bio") val bio: String?,
        @SerializedName("location") val location: String?,
        @SerializedName("total_likes") val totalLikes: Int,
        @SerializedName("total_photos") val totalPhotos: Int,
        @SerializedName("total_collections") val totalCollections: Int,
        @SerializedName("instagram_username") val instagramUsername: String?,
        @SerializedName("twitter_username") val twitterUsername: String?,
        @SerializedName("profile_image") val profileImage: ProfileImage?
    ) : Parcelable {


        @Parcelize
        data class ProfileImage(
            @SerializedName("small") val small: String,
            @SerializedName("medium") val medium: String?,
            @SerializedName("large") val large: String
        ) :
            Parcelable

        @Parcelize
        data class Links(
            @SerializedName("self") val self: String,
            @SerializedName("html") val html: String,
            @SerializedName("photos") val photos: String,
            @SerializedName("likes") val likes: String,
            @SerializedName("portfolio") val portfolio: String
        ) : Parcelable
    }

    @Parcelize
    data class Urls(
        @SerializedName("raw") val raw: String,
        @SerializedName("full") val full: String,
        @SerializedName("regular") val regular: String,
        @SerializedName("small") val small: String,
        @SerializedName("thumb") val thumb: String
    ) : Parcelable

    @Parcelize
    data class Links(
        @SerializedName("self") val self: String,
        @SerializedName("html") val html: String,
        @SerializedName("download") val download: String,
        @SerializedName("download_location") val downloadLink: String
    ) : Parcelable

    @Parcelize
    data class Tags(@SerializedName("title") var tagTitle: String) : Parcelable

}