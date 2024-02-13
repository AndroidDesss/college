package com.desss.collegeproduct.module.studentSubModule.feePay.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.studentSubModule.feePay.model.FeePayModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object FeePayRepository {
    fun getFeePay(
        activity: Activity,
        action: String,
        table: String,
        userId: String
    ): LiveData<CommonResponseModel<FeePayModel>> {
        val data: MutableLiveData<CommonResponseModel<FeePayModel>> =
            MutableLiveData<CommonResponseModel<FeePayModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<FeePayModel>?>? =
            apiService?.studentFeePay(action, table, userId)
        call?.enqueue(object : Callback<CommonResponseModel<FeePayModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<FeePayModel>?>,
                response: Response<CommonResponseModel<FeePayModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<CommonResponseModel<FeePayModel>?>, t: Throwable) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}