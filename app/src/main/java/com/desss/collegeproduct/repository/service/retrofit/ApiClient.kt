package com.desss.collegeproduct.repository.service.retrofit

import com.desss.collegeproduct.repository.service.ApiUtils
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.Protocol
import okhttp3.internal.immutableListOf
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private var retrofit: Retrofit? = null
    val httpClient: OkHttpClient
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level =
                HttpLoggingInterceptor.Level.BODY // debug build logging while prod going need to give level as "NONE" else for show data use - BODY
            return Builder()
                .protocols(immutableListOf<Protocol>(Protocol.HTTP_1_1))
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(Interceptor { chain ->
                    val request =
                        chain.request().newBuilder().addHeader("Connection", "close").build()
                    chain.proceed(request)
                })
                .addInterceptor(interceptor)
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .build()
        }
    val client: Retrofit?
        get() {
            val gson = GsonBuilder().setLenient().create()
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .client(httpClient)
                    .baseUrl(ApiUtils.COLLEGE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit
        }
}
