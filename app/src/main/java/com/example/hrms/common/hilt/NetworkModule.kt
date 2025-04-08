package com.example.hrms.signUpModule.common.hilt

import com.example.hrms.common.ApiService1
import com.example.hrms.signUpModule.domain.mapper.UserMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://192.168.4.233/"

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)  // Base URL for the API
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())  // Converts JSON responses into Kotlin objects
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService1 {
        return retrofit.create(ApiService1::class.java)
    }

    @Provides
    fun provideMapper(): UserMapper {
        return UserMapper()
    }

}