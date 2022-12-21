package com.muigorick.plashr.dataModels.users

import com.google.gson.annotations.SerializedName

class SearchUsers(
    @SerializedName("total") val totalPhotos: Int,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("results") val results: List<User>
)