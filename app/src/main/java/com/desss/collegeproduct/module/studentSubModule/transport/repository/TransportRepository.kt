package com.desss.collegeproduct.module.studentSubModule.transport.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.studentSubModule.transport.model.TransportModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object TransportRepository {

    fun getTransportData(
        activity: Activity,
        action: String,
        busNo: String
    ): LiveData<CommonResponseModel<TransportModel>> {
        val data: MutableLiveData<CommonResponseModel<TransportModel>> =
            MutableLiveData<CommonResponseModel<TransportModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<TransportModel>?>? =
            apiService?.transportData(action, busNo)
        call?.enqueue(object : Callback<CommonResponseModel<TransportModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<TransportModel>?>,
                response: Response<CommonResponseModel<TransportModel>?>
            ) {
                data.value = response.body()
            }
            override fun onFailure(call: Call<CommonResponseModel<TransportModel>?>, t: Throwable) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}