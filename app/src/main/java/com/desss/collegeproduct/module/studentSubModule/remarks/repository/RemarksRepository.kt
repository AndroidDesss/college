package com.desss.collegeproduct.module.studentSubModule.remarks.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.studentSubModule.remarks.model.RemarksModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RemarksRepository {
    fun getRemarks(
        activity: Activity,
        action: String,
        table: String,
        userId: String
    ): LiveData<CommonResponseModel<RemarksModel>> {
        val data: MutableLiveData<CommonResponseModel<RemarksModel>> =
            MutableLiveData<CommonResponseModel<RemarksModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<RemarksModel>?>? =
            apiService?.studentRemarks(action, table, userId)
        call?.enqueue(object : Callback<CommonResponseModel<RemarksModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<RemarksModel>?>,
                response: Response<CommonResponseModel<RemarksModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<CommonResponseModel<RemarksModel>?>, t: Throwable) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}