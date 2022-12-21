package com.muigorick.plashr.dataModels.users

import com.google.gson.annotations.SerializedName

data class LoggedUser(
    @SerializedName("id") val userId: String,
    @SerializedName("username") val username: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("total_likes") val totalLikes: Long,
    @SerializedName("total_photos") val totalPhotos: Long,
    @SerializedName("total_collections") val totalCollections: Long,
    @SerializedName("instagram_username") val instagramUsername: String,
    @SerializedName("twitter_username") val twitterUsername: String
)