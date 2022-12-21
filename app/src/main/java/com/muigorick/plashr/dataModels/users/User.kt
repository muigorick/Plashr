package com.muigorick.plashr.dataModels.users

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("id") val userId: String,
    @SerializedName("username") val username: String,
    @SerializedName("name") val name: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("bio") val bio: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("profile_image") val profileImage: ProfileImage,
    @SerializedName("total_likes") val totalLikes: Int,
    @SerializedName("total_photos") val totalPhotos: Int,
    @SerializedName("total_collections") val totalCollections: Int,
    @SerializedName("followed_by_user") val followedByUser: Boolean,
    @SerializedName("followers_count") val followersCount: String,
    @SerializedName("following_count") val followingCount: String,
    @SerializedName("instagram_username") val instagramUsername: String,
    @SerializedName("twitter_username") val twitterUsername: String,
) : Parcelable {

    //Our attribution URL as required by the unsplash guidelines
    val attributionUrl get() = "https://unsplash.com/@$username?utm_source=Plashr&utm_medium=referral"

    //Profile Image Class
    @Parcelize
    data class ProfileImage(
        @SerializedName("small") val smallProfileImage: String,
        @SerializedName("medium") val mediumProfileImage: String,
        @SerializedName("large") val largeProfileImage: String
    ) : Parcelable
}