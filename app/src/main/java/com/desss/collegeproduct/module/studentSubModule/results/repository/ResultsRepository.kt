package com.desss.collegeproduct.module.studentSubModule.results.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.studentSubModule.results.model.ResultsModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ResultsRepository {

    fun getResults(
        activity: Activity,
        action: String,
        table: String,
        timeTableId: String,
        userId: String
    ): LiveData<CommonResponseModel<ResultsModel>> {
        val data: MutableLiveData<CommonResponseModel<ResultsModel>> =
            MutableLiveData<CommonResponseModel<ResultsModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<ResultsModel>?>? =
            apiService?.studentResults(action, table, timeTableId, userId)
        call?.enqueue(object : Callback<CommonResponseModel<ResultsModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<ResultsModel>?>,
                response: Response<CommonResponseModel<ResultsModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<CommonResponseModel<ResultsModel>?>, t: Throwable) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}