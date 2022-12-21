package com.muigorick.plashr.dataModels.photos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoLocation(
    @SerializedName("city") val city: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("position") val position: Position
) :
    Parcelable {
    @Parcelize
    data class Position(
        @SerializedName("latitude") val latitude: Float,
        @SerializedName("longitude") val longitude: Float?,
    ) :
        Parcelable

    val formattedLocation: String?
        get() {
            return if (city == null && country == null) {
                null
            } else if (city != null && country == null) {
                city
            } else if (city == null && country != null) {
                country
            } else "$city, $country"

        }
}
