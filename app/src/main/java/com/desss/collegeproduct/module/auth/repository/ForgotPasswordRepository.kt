package com.desss.collegeproduct.module.auth.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.auth.model.ForgotModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ForgotPasswordRepository {

    fun getOtp(
        activity: Activity,
        action: String,
        email: String
    ): LiveData<CommonResponseModel<ForgotModel>> {
        val data: MutableLiveData<CommonResponseModel<ForgotModel>> =
            MutableLiveData<CommonResponseModel<ForgotModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<ForgotModel>?>? = apiService?.getOtp(action, email)
        call?.enqueue(object : Callback<CommonResponseModel<ForgotModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<ForgotModel>?>,
                response: Response<CommonResponseModel<ForgotModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<CommonResponseModel<ForgotModel>?>, t: Throwable) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}