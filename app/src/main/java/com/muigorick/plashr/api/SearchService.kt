package com.muigorick.plashr.api

import com.muigorick.plashr.dataModels.collections.SearchCollections
import com.muigorick.plashr.dataModels.photos.SearchPhotos
import com.muigorick.plashr.dataModels.users.SearchUsers
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    /**
     * Photo search function
     *
     * @return A searchPhotos model.
     */
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): SearchPhotos

    /**
     *  Collection search function.
     *
     *  @return A searchCollections model.
     */
    @GET("search/collections")
    suspend fun searchCollections(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): SearchCollections


    /**
     *  User search function.
     *
     *  @return A searchUsers model.
     */
    @GET("search/users")
    suspend fun searchUsers(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): SearchUsers
}