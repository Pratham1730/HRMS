package com.example.hrms

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    fun getInstance(baseUrl: String): ApiService {
        // Create logging interceptor to log request and response details
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY  // Log request and response bodies
        }

        // Create an interceptor to add custom headers (e.g., Authorization)
//        val headerInterceptor = Interceptor { chain ->
//            val newRequest: Request = chain.request().newBuilder()
//                .addHeader("Authorization", "Bearer $authToken")  // Add Bearer Token for Auth
//                .build()
//            chain.proceed(newRequest)
//        }

        // Add logging interceptor and header interceptor to OkHttpClient
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)  // Attach logging interceptor
            //.addInterceptor(headerInterceptor)   // Attach header interceptor
            .readTimeout(30, TimeUnit.SECONDS)   // Set read timeout to 30 seconds
            .writeTimeout(30, TimeUnit.SECONDS)  // Set write timeout to 30 seconds
            .connectTimeout(30, TimeUnit.SECONDS) // Set connect timeout to 30 seconds
            .build()

        // Build Retrofit instance with the custom OkHttpClient
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)  // Use OkHttpClient with logging and interceptors
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}