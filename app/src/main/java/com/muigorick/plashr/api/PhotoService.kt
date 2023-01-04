package com.muigorick.plashr.api

import com.muigorick.plashr.dataModels.photos.Photo
import retrofit2.Call
import retrofit2.http.*

interface PhotoService {

    /**
     * Gets a photo using the ID.
     *
     * @return A photo model call.
     */
    @GET("photos/{id}")
    fun getPhoto(
        @Path("id") id: String,
    ): Call<Photo>

    /**
     * Gets a list of the editorial photos.
     *
     * @return A list of all editorial photos.
     */
    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("order_by") order_by: String
    ): List<Photo>

    /**
     * Gets a random photo.
     *
     * @return A random photo.
     */
    @GET("photos/random")
    fun getRandomPhoto(
        @Query("orientation") orientation: String
    ): Call<Photo>


    /**
     * Gets a photo using the ID.
     *
     * @return A photo model call.
     */
    @GET("photos/{id}/statistics")
    fun getPhotoStatistics(
        @Path("id") id: String,
        @Query("resolution") resolution: String,
        @Query("quantity") quantity: Int
    ): Call<Photo>
}