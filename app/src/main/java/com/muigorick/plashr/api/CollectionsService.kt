package com.muigorick.plashr.api

import com.muigorick.plashr.dataModels.collections.Collection
import com.muigorick.plashr.dataModels.photos.Photo
import retrofit2.Call
import retrofit2.http.*

interface CollectionsService {

    // Gets collections
    @GET("collections")
    suspend fun getCollections(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): List<Collection>


    // Gets a collection's details
    @GET("collections/{id}")
    fun getCollection(@Path("id") id: String?)
    : Call<Collection>

    // Gets a collection's photos
    @GET("collections/{id}/photos")
    suspend fun getCollectionPhotos(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): List<Photo>

    // Gets a collection's related collections
    @GET("collections/{id}/related")
    fun getRelatedCollections(@Path("id") id: String?): Call<List<Collection>>

    // Creates a new collection
    @POST("collections")
    fun createNewCollection(
        @Query("title") title: String?,
        @Query("description") description: String?,
        @Query("private") isPrivate: Boolean
    ): Call<Collection>

    // Updates an existing collection
    @PUT("collections/{id}")
    fun updateCollection(
        @Path("id") id: String?,
        @Query("title") title: String?,
        @Query("description") description: String?,
        @Query("private") isPrivate: Boolean
    ): Call<Collection>

    // Updates an existing collection
    @DELETE("collections/{id}")
    fun deleteCollection(@Path("id") id: String?): Call<Collection?>?

    @POST("collections/{collection_id}/add")
    fun addPhotoToCollection(
        @Path("collection_id") collectionId: String?,
        @Query("photo_id") photoId: String?
    ): Call<Collection>

    @POST("collections/{collection_id}/remove")
    fun removePhotoFromCollection(
        @Path("collection_id") collectionId: String?,
        @Query("photo_id") photoId: String?
    ): Call<Collection>

}