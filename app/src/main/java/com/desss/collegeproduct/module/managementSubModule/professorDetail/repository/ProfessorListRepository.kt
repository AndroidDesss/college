package com.desss.collegeproduct.module.managementSubModule.professorDetail.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.managementSubModule.professorDetail.model.TotalProfessorModel
import com.desss.collegeproduct.module.professorSubModule.schedule.model.ViewScheduleModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ProfessorListRepository {

    fun getProfessorList(
        activity: Activity,
        action: String,
        table: String,
        rollId: String
    ): LiveData<CommonResponseModel<TotalProfessorModel>> {
        val data: MutableLiveData<CommonResponseModel<TotalProfessorModel>> =
            MutableLiveData<CommonResponseModel<TotalProfessorModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<TotalProfessorModel>?>? =
            apiService?.professorList(action, table, rollId)
        call?.enqueue(object : Callback<CommonResponseModel<TotalProfessorModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<TotalProfessorModel>?>,
                response: Response<CommonResponseModel<TotalProfessorModel>?>
            ) {
                data.value = response.body()
            }
            override fun onFailure(
                call: Call<CommonResponseModel<TotalProfessorModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }

    fun getScheduleValues(
        activity: Activity,
        action: String,
        table: String,
        userId: String,
        date: String
    ): LiveData<CommonResponseModel<ViewScheduleModel>> {
        val data: MutableLiveData<CommonResponseModel<ViewScheduleModel>> =
            MutableLiveData<CommonResponseModel<ViewScheduleModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<ViewScheduleModel>?>? =
            apiService?.getViewScheduleByDate(action, table, userId,date)
        call?.enqueue(object : Callback<CommonResponseModel<ViewScheduleModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<ViewScheduleModel>?>,
                response: Response<CommonResponseModel<ViewScheduleModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<ViewScheduleModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}