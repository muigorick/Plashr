package com.muigorick.plashr.dataModels.login

import com.google.gson.annotations.SerializedName

data class OAuthModel(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("scope") val scope: String,
    @SerializedName("created_at") val createdAt: Long
)