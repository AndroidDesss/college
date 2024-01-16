package com.desss.collegeproduct.repository.service

import com.desss.collegeproduct.module.auth.model.LoginModel
import retrofit2.Call
import retrofit2.http.*


interface ApiServices {
    @Headers(*["Content-Type: application/json;charset=UTF-8"])
    @GET("dynamic/dynamicapi.php")
    fun loginSubmit(@Query("action") action: String?, @Query("table") accountsUser: String?, @Query("user_id") userId: String?, @Query("password") password: String?
    ): Call<LoginModel?>?
//
//    @Headers(*["Content-Type: application/json;charset=UTF-8"])
//    @GET("StudentCourseMaterial/GetTopicAndMaterialDetail")
//    fun getTopicAndMaterialDetails(
//        @Header("Authorization") authHeader: String?,
//        @Query("regId") regId: String?,
//        @Query("fromdevice") fromdevice: Boolean
//    ): Call<TopicAndMaterialDetailModel?>?
}