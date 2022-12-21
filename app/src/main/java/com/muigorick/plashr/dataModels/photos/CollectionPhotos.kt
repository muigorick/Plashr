package com.muigorick.plashr.dataModels.photos

import com.google.gson.annotations.SerializedName

data class CollectionPhotos(
    @SerializedName("results") val results: List<Photo>
)
