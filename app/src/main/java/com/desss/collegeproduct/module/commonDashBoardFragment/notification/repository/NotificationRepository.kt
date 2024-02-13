package com.desss.collegeproduct.module.commonDashBoardFragment.notification.repository

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.commonDashBoardFragment.notification.model.NotificationModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object NotificationRepository {

    fun notification(
        activity: Activity,
        action: String,
        table: String
    ): LiveData<CommonResponseModel<NotificationModel>> {
        val data: MutableLiveData<CommonResponseModel<NotificationModel>> =
            MutableLiveData<CommonResponseModel<NotificationModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<NotificationModel>?>? =
            apiService?.notification(action, table)
        call?.enqueue(object : Callback<CommonResponseModel<NotificationModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<NotificationModel>?>,
                response: Response<CommonResponseModel<NotificationModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<NotificationModel>?>,
                t: Throwable
            ) {
                Log.d("errorResponse", t.message.toString())
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}