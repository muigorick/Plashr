package com.muigorick.plashr.network

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.muigorick.plashr.api.*
import com.muigorick.plashr.ui.activities.settings.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val baseURL = "https://api.unsplash.com/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(AuthenticationInterceptor())
            .build()
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(httpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoginDataService(retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)

    @Provides
    @Singleton
    fun providePhotoDataService(retrofit: Retrofit): PhotoService =
        retrofit.create(PhotoService::class.java)

    @Provides
    @Singleton
    fun provideCollectionsDataService(retrofit: Retrofit): CollectionsService =
        retrofit.create(CollectionsService::class.java)

    @Provides
    @Singleton
    fun provideSearchDataService(retrofit: Retrofit): SearchService =
        retrofit.create(SearchService::class.java)

    @Provides
    @Singleton
    fun provideTopicsDataService(retrofit: Retrofit): TopicsService =
        retrofit.create(TopicsService::class.java)

    @Provides
    @Singleton
    fun provideUserDataService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideDataStoreRepository(@ApplicationContext context: Context) =
        DataStoreRepository(context)
}