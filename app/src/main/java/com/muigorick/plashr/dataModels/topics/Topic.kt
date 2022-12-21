package com.muigorick.plashr.dataModels.topics

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Topic(
    @SerializedName("id") val id: String,
    @SerializedName("slug") val slugs: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("published_at") val publishedAt: String,
    @SerializedName("links") val links: Links,
    @SerializedName("updated_at") val uploadedAt: String,
    @SerializedName("starts_at") val startsAt: String,
    @SerializedName("ends_at") val endsAt: String?,
    @SerializedName("only_submissions_after") val onySubmissionsAfter: String?,
    @SerializedName("featured") val featured: Boolean,
    @SerializedName("total_photos") val totalPhotos: Int,
    @SerializedName("status") val status: String,
    @SerializedName("preview_photos") val previewPhotos: List<PreviewPhotos>?,
    @SerializedName("owners") val owners: List<Owners>?,
    @SerializedName("top_contributors") val topContributors: List<TopContributors>?,
    @SerializedName("cover_photo") val coverPhoto: CoverPhoto
) : Parcelable {

    //Our attribution URL as required by the unsplash guidelines
    val attributionUrl get() = "https://unsplash.com/t/$id?utm_source=Plashr&utm_medium=referral"

    // Top Contributors Class
    @Parcelize
    data class TopContributors(
        @SerializedName("id") val id: String,
        @SerializedName("updated_at") val updatedAt: String,
        @SerializedName("username") val username: String,
        @SerializedName("name") val name: String,
        @SerializedName("first_name") val firstName: String,
        @SerializedName("last_name") val lastName: String?,
        @SerializedName("twitter_username") val twitterUsername: String,
        @SerializedName("portfolio_url") val portfolioUrl: String?,
        @SerializedName("bio") val bio: String?,
        @SerializedName("location") val location: String?,
        @SerializedName("links") val links: Links,
        @SerializedName("instagram_username") val instagramUsername: String?,
        @SerializedName("total_collections") val totalCollections: Int,
        @SerializedName("total_likes") val totalLikes: Int,
        @SerializedName("total_photos") val totalPhotos: Int,
        @SerializedName("accepted_tos") val acceptedTOS: Boolean,
        @SerializedName("profile_image") val profilePhotos: ProfileImage
    ) : Parcelable {
        @Parcelize
        data class Links(
            @SerializedName("self") val self: String,
            @SerializedName("html") val html: String,
            @SerializedName("photos") val photos: String,
            @SerializedName("likes") val likes: String,
            @SerializedName("portfolio") val portfolio: String,
            @SerializedName("following") val following: String,
            @SerializedName("followers") val followers: String
        ) : Parcelable

        // Owner Profile Image Class
        @Parcelize
        data class ProfileImage(
            @SerializedName("small") val smallProfileImage: String,
            @SerializedName("medium") val mediumProfileImage: String,
            @SerializedName("large") val largeProfileImage: String
        ) : Parcelable
    }


    val dateUploaded: String
        get() {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formatTo = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            return formatTo.format(formatter.parse(publishedAt)!!)
        }

    // Topic Links Class
    @Parcelize
    data class Links(
        @SerializedName("self") val self: String,
        @SerializedName("html") val html: String,
        @SerializedName("photos") val photos: String
    ) : Parcelable

    //Owner Class
    @Parcelize
    data class Owners(
        @SerializedName("id") val id: String,
        @SerializedName("updated_at") val updatedAt: String,
        @SerializedName("username") val username: String,
        @SerializedName("name") val name: String,
        @SerializedName("first_name") val firstName: String,
        @SerializedName("last_name") val lastName: String?,
        @SerializedName("twitter_username") val twitterUsername: String,
        @SerializedName("portfolio_url") val portfolioUrl: String?,
        @SerializedName("bio") val bio: String?,
        @SerializedName("location") val location: String?,
        @SerializedName("links") val links: Links,
        @SerializedName("instagram_username") val instagramUsername: String?,
        @SerializedName("total_collections") val totalCollections: Int,
        @SerializedName("total_likes") val totalLikes: Int,
        @SerializedName("total_photos") val totalPhotos: Int,
        @SerializedName("accepted_tos") val acceptedTOS: Boolean,
        @SerializedName("profile_image") val profilePhotos: ProfileImage
    ) : Parcelable {
        @Parcelize
        data class Links(
            @SerializedName("self") val self: String,
            @SerializedName("html") val html: String,
            @SerializedName("photos") val photos: String,
            @SerializedName("likes") val likes: String,
            @SerializedName("portfolio") val portfolio: String,
            @SerializedName("following") val following: String,
            @SerializedName("followers") val followers: String
        ) : Parcelable

        // Owner Profile Image Class
        @Parcelize
        data class ProfileImage(
            @SerializedName("small") val smallProfileImage: String,
            @SerializedName("medium") val mediumProfileImage: String,
            @SerializedName("large") val largeProfileImage: String
        ) : Parcelable
    }

    //TODO Implement the code commented below.
    /*"current_user_contributions": [],
       "total_current_user_submissions":{},
       */


    @Parcelize
    data class CoverPhoto(
        @SerializedName("id") val id: String,
        @SerializedName("created_at") val createdAt: String,
        @SerializedName("updated_at") val updatedAt: String,
        @SerializedName("promoted_at") val promotedAt: String?,
        @SerializedName("width") val width: Int,
        @SerializedName("height") val height: Int,
        @SerializedName("color") val color: String,
        @SerializedName("blur_hash") val blurHash: String,
        @SerializedName("description") val description: String?,
        @SerializedName("alt_description") val altDescription: String?,
        @SerializedName("urls") val urls: Urls,
        @SerializedName("links") val links: Links
    ) : Parcelable {

        /**
         * Converts and Returns a ColorDrawable from the color string we get from the API
         */
        val coverPhotoColor: ColorDrawable
            get():ColorDrawable {
                val color = Color.parseColor(color)
                return ColorDrawable(color)
            }

        @Parcelize
        data class Urls(
            @SerializedName("raw") val raw: String,
            @SerializedName("full") val full: String,
            @SerializedName("regular") val regular: String,
            @SerializedName("small") val small: String,
            @SerializedName("thumb") val thumb: String,
        ) : Parcelable

        @Parcelize
        data class Links(
            @SerializedName("self") val self: String,
            @SerializedName("html") val html: String,
            @SerializedName("download") val download: String,
            @SerializedName("download_location") val downloadLocation: String,
        ) : Parcelable
    }


    /***
     * A preview list of 4 photos.
     * */
    @Parcelize
    data class PreviewPhotos(
        @SerializedName("id") val id: String,
        @SerializedName("created_at") val createdAt: String,
        @SerializedName("updated_at") val updatedAt: String,
        @SerializedName("urls") val urls: Urls
    ) : Parcelable {
        @Parcelize
        data class Urls(
            @SerializedName("raw") val raw: String,
            @SerializedName("full") val full: String,
            @SerializedName("regular") val regular: String,
            @SerializedName("small") val small: String,
            @SerializedName("thumb") val thumb: String,
        ) : Parcelable

    }
}


