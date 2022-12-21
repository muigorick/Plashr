package com.muigorick.plashr.network

import android.util.Log
import com.muigorick.plashr.BuildConfig
import com.muigorick.plashr.account.AccountManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthenticationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request
        if (AccountManager().getInstance()?.isAppAuthorized() == true) {
            request = chain.request().newBuilder().addHeader(
                "Authorization",
                "Bearer" + AccountManager().getInstance()?.getAccessToken()
            )
                .build()
            Log.i(
                "Interceptor ",
                " Authorization bearer: Token bearer call made"
            )

        } else {
            request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Client-ID ${BuildConfig.ACCESS_KEY}")
                .build()
            Log.i(
                "Interceptor ",
                "Authorization client ID: client ID call made"
            )
        }

        return chain.proceed(request)
    }
}