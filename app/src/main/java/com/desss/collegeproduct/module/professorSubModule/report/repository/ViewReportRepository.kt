package com.desss.collegeproduct.module.professorSubModule.report.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.professorSubModule.report.model.ViewReportsModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ViewReportRepository {
    fun getReportsValues(
        activity: Activity,
        action: String,
        table: String,
        proUserId: String,
        status: String
    ): LiveData<CommonResponseModel<ViewReportsModel>> {
        val data: MutableLiveData<CommonResponseModel<ViewReportsModel>> =
            MutableLiveData<CommonResponseModel<ViewReportsModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<ViewReportsModel>?>? =
            apiService?.getReportsListByProfessor(action, table, proUserId, status)
        call?.enqueue(object : Callback<CommonResponseModel<ViewReportsModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<ViewReportsModel>?>,
                response: Response<CommonResponseModel<ViewReportsModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<ViewReportsModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}