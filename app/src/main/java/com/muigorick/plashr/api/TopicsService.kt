package com.muigorick.plashr.api

import com.muigorick.plashr.dataModels.photos.Photo
import com.muigorick.plashr.dataModels.topics.Topic
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TopicsService {

    /**
     * Gets topics.
     *
     * @return A list of topics.
     */
    @GET("topics")
    suspend fun getTopics(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("order_by") order_by: String
    ): List<Topic>

    /**
     * Gets a topic using the ID.
     *
     * @return A topic model call.
     */
    @GET("/topics/{id}")
    fun getTopic(
        @Path("id") id: String,
    ): Call<Topic>


    /**
     * Gets a topic's photos using the ID.
     *
     * @return A list of photos
     */
    @GET("/topics/{id}/photos")
    suspend fun getTopicPhotos(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): List<Photo>

}