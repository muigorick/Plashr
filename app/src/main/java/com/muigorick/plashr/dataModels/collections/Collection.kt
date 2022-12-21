package com.muigorick.plashr.dataModels.collections

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.NumberFormat
import java.util.*

@Parcelize
data class Collection(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("published_at") val published_at: String?,
    @SerializedName("total_photos") val totalPhotos: Int?,
    @SerializedName("private") val private: Boolean?,
    @SerializedName("cover_photo") val coverPhoto: CoverPhoto?,
    @SerializedName("user") val user: User?,
    @SerializedName("links") val links: Links?
) : Parcelable {

    // Our attribution URL as required by the unsplash guidelines
    val attributionUrl get() = "https://unsplash.com/collections/$id?utm_source=Plashr&utm_medium=referral"

    // This formats the total photos count to the user's default Locale.
    val totalPhotosCountFormatted: String
        get():String {
            return NumberFormat.getNumberInstance(Locale.getDefault()).format(totalPhotos)
        }

    @Parcelize
    data class User(
        @SerializedName("id") val id: String,
        @SerializedName("username") val username: String,
        @SerializedName("name") val name: String,
        @SerializedName("portfolio_url") val portfolioURL: String?,
        @SerializedName("bio") val bio: String?,
        @SerializedName("location") val location: String?,
        @SerializedName("profile_image") val profileImage: ProfileImage?,
        @SerializedName("total_likes") val totalLikes: Int,
        @SerializedName("total_photos") val totalPhotos: Int,
        @SerializedName("total_collections") val totalCollections: Int,
        @SerializedName("instagram_username") val instagramUsername: String?,
        @SerializedName("twitter_username") val twitterUsername: String?,
    ) : Parcelable {
        @Parcelize
        data class ProfileImage(
            @SerializedName("small") val small: String,
            @SerializedName("medium") val medium: String,
            @SerializedName("large") val large: String
        ) : Parcelable
    }

    @Parcelize
    data class CoverPhoto(
        @SerializedName("id") val id: String,
        @SerializedName("urls") val urls: Urls
    ) : Parcelable {

        @Parcelize
        data class Urls(
            @SerializedName("raw") val raw: String,
            @SerializedName("full") val full: String,
            @SerializedName("regular") val regular: String,
            @SerializedName("small") val small: String,
            @SerializedName("thumb") val thumb: String
        ) : Parcelable
    }

    @Parcelize
    data class Links(
        @SerializedName("self") val self: String,
        @SerializedName("html") val html: String,
        @SerializedName("photos") val photos: String,
    ) : Parcelable
}