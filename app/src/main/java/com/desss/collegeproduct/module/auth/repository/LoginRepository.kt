package com.desss.collegeproduct.module.auth.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.auth.model.LoginModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object LoginRepository {
    fun submitLogin(
        activity: Activity,
        action: String,
        email: String,
        password: String
    ): LiveData<CommonResponseModel<LoginModel>> {
        val data: MutableLiveData<CommonResponseModel<LoginModel>> =
            MutableLiveData<CommonResponseModel<LoginModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<LoginModel>?>? = apiService?.loginSubmit(toRequestBody(action), toRequestBody(email), toRequestBody(password))
        call?.enqueue(object : Callback<CommonResponseModel<LoginModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<LoginModel>?>,
                response: Response<CommonResponseModel<LoginModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<LoginModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }

    private fun toRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }
}