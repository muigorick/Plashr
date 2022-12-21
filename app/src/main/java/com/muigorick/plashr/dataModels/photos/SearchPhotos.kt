package com.muigorick.plashr.dataModels.photos

import com.google.gson.annotations.SerializedName

data class SearchPhotos(
    @SerializedName("total") val totalPhotos: Int,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("results") val results: List<Photo>
)