package com.desss.collegeproduct.repository.service.retrofit

import android.content.Context
import android.util.Log
import com.codewith.kotlinretrofit.util.IntegerDefaultAdapter
import com.desss.collegeproduct.repository.service.ApiResponseListener
import com.desss.collegeproduct.repository.service.ApiServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.*


class RetrofitConnect : Observable(), ApiResponseListener {
    lateinit var apiServices: ApiServices
    private val context: Context? = null
    private var baseUrlLocal: String? = null
    private var requestToken: String? = null
    private var apiResponseListenerToken: ApiResponseListener? = null

    companion object {
        var INSTANCE = RetrofitConnect()
        fun getInstance(): RetrofitConnect? {
            return RetrofitConnect.INSTANCE
        }
    }


    fun getRetrofitApiService(baseUrl: String): ApiServices {
        val interceptor = HttpLoggingInterceptor()
        baseUrlLocal = baseUrl
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY // debug build logging
        } else {
            interceptor.level =
                HttpLoggingInterceptor.Level.NONE // changed BODY to NONE to avoid OOM(Out of Memory) issue
        }
            val client = SSLHandler.getUnsafeOkHttpClient().addInterceptor(Interceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder().method(original.method, original.body)
                        .build()
                    return@Interceptor chain.proceed(request)
                })
                .addInterceptor(HeaderInterceptor(context))
                .addInterceptor(AddCookiesInterceptor())
                .addInterceptor(ReceivedCookiesInterceptor())
                .addInterceptor(interceptor)
                .connectTimeout(180, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(180, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(180, java.util.concurrent.TimeUnit.SECONDS)
                .build()
            apiServices = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(ToStringConverterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiServices::class.java)

        return apiServices
    }


    fun <T> addRetrofitCalls(
        baseCall: retrofit2.Call<T>,
        apiResponseListener: ApiResponseListener,
        request: String
    ) {
        try {
            System.gc()
            baseCall.enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>?, t: Throwable?) {
                    try {
                        System.gc()
                        apiResponseListener.onReceiveError(request, t.toString())
                    } catch (e: Exception) {
                            e.printStackTrace()

                    }

                }

                override fun onResponse(call: Call<T>?, response: Response<T>?) {

                    if (response?.body() != null) {
                        when {
                            response.code() == 200 -> {
                                    requestToken = request
                                    apiResponseListenerToken = apiResponseListener
                                        apiResponseListener.onReceiveResult(request, response)

                            }
                            response.code() == 201 -> {
                                apiResponseListener.onReceiveResult(request, response)
                            }
                            else -> {
                                apiResponseListener.onReceiveError(request, response.toString())
                            }
                        }
                    } else if (response?.errorBody() != null) {
                        if (response.code() == 504) {
//
                            apiResponseListener.onReceiveError(
                                request,
                                response.errorBody()!!.string()
                            )
                        }
                    }
                }
            })
        } catch (e: Exception) {
                e.printStackTrace()
        }

    }


    @Throws(IOException::class)
    fun <T> dataConvertor(response: retrofit2.Response<*>, cls: Class<T>): T? {
        val cmmLoginParser: T?
        System.gc()
        if (response.body() is String) {
            cmmLoginParser = gsonMapper().fromJson(response.body()!!.toString(), cls)
        } else {
            cmmLoginParser = (response as retrofit2.Response<T>).body()
        }
        return cmmLoginParser
    }

    @Throws(IOException::class)
    fun <T> dataConvertors(response: retrofit2.Response<*>, cls: Class<T>): List<T>? {
        val cmmLoginParser: List<T>?
        System.gc()
        if (response.body() is String) {
            cmmLoginParser = listOf(gsonMapper().fromJson(response.body()?.toString(), cls))
        } else {
            cmmLoginParser = (response as retrofit2.Response<List<T>>).body()
        }
        return cmmLoginParser
    }

    fun gsonMapper(): Gson {
        System.gc()
        return GsonBuilder()
            .registerTypeAdapter(Int::class.java, IntegerDefaultAdapter())
            .registerTypeAdapter(Int::class.javaPrimitiveType, IntegerDefaultAdapter())
            .serializeNulls()
            .create()
    }

    fun <T> addRetrofitCallsList(
        baseCall: Call<List<T?>>,
        apiResponseListener: ApiResponseListener,
        request: String
    ) {
        baseCall.enqueue(object : Callback<List<T?>> {
            override fun onFailure(call: Call<List<T?>>, t: Throwable) {
                apiResponseListener.onReceiveError("1", t.toString())
            }

            override fun onResponse(call: Call<List<T?>>, response: Response<List<T?>>) {
                if (response.body() != null) {
                    if (response.code() == 200) {
                        apiResponseListener.onReceiveResult(request, response)
                    }
                }
            }
        })
    }

}