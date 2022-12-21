package com.muigorick.plashr.dataModels.photos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class PhotoTags(
    @SerializedName("title")
    val title: String?
) : Parcelable