package com.desss.collegeproduct.module.commonDashBoardFragment.profile.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.commonDashBoardFragment.profile.model.ProfileModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ProfileRepository {

    fun studentProfileData(
        activity: Activity,
        action: String,
        table: String,
        id: String
    ): LiveData<CommonResponseModel<ProfileModel>> {
        val data: MutableLiveData<CommonResponseModel<ProfileModel>> =
            MutableLiveData<CommonResponseModel<ProfileModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<ProfileModel>?>? = apiService?.profile(action, table, id)
        call?.enqueue(object : Callback<CommonResponseModel<ProfileModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<ProfileModel>?>,
                response: Response<CommonResponseModel<ProfileModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<CommonResponseModel<ProfileModel>?>, t: Throwable) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}