package com.muigorick.plashr.api

import com.muigorick.plashr.dataModels.collections.Collection
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
     * Gets a list of the editorial photos. To be specific, these are the photos that you see on the unsplash website homepage.
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
     * Adds a photo to the user's chosen collection
     *
     * @return an updated version of the collection into which the user added the photo.
     */
    @POST("collections/{collection_id}/add")
    suspend fun addPhotoToCollections(
        @Path("collection_id") collectionId: String,
        @Query("photo_id") photoId: String
    ): Collection

    /**
     * Deletes a photo to the user's chosen collection
     *
     * @return an updated version of the collection into which the user removed the photo.
     */
    @DELETE("collections/{collection_id}/remove")
    suspend fun removePhotoFromCollections(
        @Path("collection_id") collectionId: String,
        @Query("photo_id") photoId: String
    ): Collection

}