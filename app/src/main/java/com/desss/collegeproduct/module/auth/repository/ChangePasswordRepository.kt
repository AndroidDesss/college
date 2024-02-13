package com.desss.collegeproduct.module.auth.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.auth.model.ChangePasswordModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ChangePasswordRepository {
    fun getUpdatePassword(
        activity: Activity,
        action: String,
        email: String,
        password: String
    ): LiveData<CommonResponseModel<ChangePasswordModel>> {
        val data: MutableLiveData<CommonResponseModel<ChangePasswordModel>> =
            MutableLiveData<CommonResponseModel<ChangePasswordModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<ChangePasswordModel>?>? =
            apiService?.getChangePassword(action, email, password)
        call?.enqueue(object : Callback<CommonResponseModel<ChangePasswordModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<ChangePasswordModel>?>,
                response: Response<CommonResponseModel<ChangePasswordModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<ChangePasswordModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}