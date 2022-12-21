package com.muigorick.plashr.api

import com.muigorick.plashr.dataModels.collections.Collection
import com.muigorick.plashr.dataModels.photos.Photo
import com.muigorick.plashr.dataModels.users.LoggedUser
import com.muigorick.plashr.dataModels.users.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    /**
     *  Gets a logged user public profile.
     *
     *  @return A Logged User model call.
     */
    @GET("me")
    fun getLoggedUserProfile(): Call<LoggedUser>

    /**
     *  Gets a  User public profile. In this instance the user is not logged in. This is what we use to get other user profile.
     *
     *  @return A User model call.
     */
    @GET("users/{username}")
    fun getUserProfile(@Path("username") username: String): Call<User>

    /**
     * Gets a user's photos.
     *
     * @return A list of all the users photos. This are photos the user has uploaded themselves.
     */
    @GET("users/{username}/photos")
    suspend fun getUserPhotos(
        @Path("username") username: String,
        @Query("page") page: Long,
        @Query("per_page") per_page: Long
    ): List<Photo>

    /**
     * Gets a user's liked photos.
     *
     * @return A list of all the photos a user has liked.
     */
    @GET("users/{username}/likes")
    suspend fun getUserLikes(
        @Path("username") username: String,
        @Query("page") page: Long,
        @Query("per_page") per_page: Long
    ): List<Photo>

    /**
     * Gets a user's collections.
     *
     * @return A list of all the collections the user has created.
     */
    @GET("users/{username}/collections")
    suspend fun getUserCollections(
        @Path("username") username: String,
        @Query("page") page: Long,
        @Query("per_page") per_page: Long
    ): List<Collection>

    /**
     * TODO To be updated. (reason = "I need to refer to the documentation first")
     *
     * @return To be updated.
     */
    @GET("users/{username}/statistics")
    fun getUserStatistics(
        @Path("username") username: String,
        @Query("resolution") page: Long,
        @Query("quantity") per_page: Long
    ): List<Photo>
}