package com.muigorick.plashr.dataModels.collections

import com.google.gson.annotations.SerializedName

class SearchCollections(
    @SerializedName("total") val totalCollections: Int,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("results") val results: List<Collection>
)