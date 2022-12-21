package com.muigorick.plashr.dataModels.photos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
@Parcelize
data class PhotoExif(
@SerializedName("make") val make: String?,
@SerializedName("model") val model: String?,
@SerializedName("exposure_time") val exposure_time: String?,
@SerializedName("aperture") val aperture: String?,
@SerializedName("focal_length") val focal_length : String?,
@SerializedName("iso") val iso: Int?,
) : Parcelable