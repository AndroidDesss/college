package com.desss.collegeproduct.module.professorSubModule.professorAttendance.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.professorSubModule.professorAttendance.model.CheckProfessorAttendanceModel
import com.desss.collegeproduct.module.professorSubModule.professorAttendance.model.ProfessorCountModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ProfessorAttendanceRepository {

    fun getProfessorAttendanceApi(
        activity: Activity,
        action: String,
        userId: String
    ): LiveData<CommonResponseModel<CheckProfessorAttendanceModel>> {
        val data: MutableLiveData<CommonResponseModel<CheckProfessorAttendanceModel>> =
            MutableLiveData<CommonResponseModel<CheckProfessorAttendanceModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<CheckProfessorAttendanceModel>?>? =
            apiService?.getProfessorAttendance(action, userId)
        call?.enqueue(object : Callback<CommonResponseModel<CheckProfessorAttendanceModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<CheckProfessorAttendanceModel>?>,
                response: Response<CommonResponseModel<CheckProfessorAttendanceModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<CheckProfessorAttendanceModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }

    fun getProfessorCountApi(
        activity: Activity,
        action: String,
        userId: String,
        month: String,
        year: String
    ): LiveData<CommonResponseModel<ProfessorCountModel>> {
        val data: MutableLiveData<CommonResponseModel<ProfessorCountModel>> =
            MutableLiveData<CommonResponseModel<ProfessorCountModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<ProfessorCountModel>?>? =
            apiService?.getProfessorAttendanceCount(action, userId, month, year)
        call?.enqueue(object : Callback<CommonResponseModel<ProfessorCountModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<ProfessorCountModel>?>,
                response: Response<CommonResponseModel<ProfessorCountModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<ProfessorCountModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }

    fun getMarkProfessorAttendanceApi(
        activity: Activity,
        action: String,
        userId: String
    ): LiveData<CommonResponseModel<CheckProfessorAttendanceModel>> {
        val data: MutableLiveData<CommonResponseModel<CheckProfessorAttendanceModel>> =
            MutableLiveData<CommonResponseModel<CheckProfessorAttendanceModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<CheckProfessorAttendanceModel>?>? =
            apiService?.getMarkProfessorAttendanceCount(action, userId)
        call?.enqueue(object : Callback<CommonResponseModel<CheckProfessorAttendanceModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<CheckProfessorAttendanceModel>?>,
                response: Response<CommonResponseModel<CheckProfessorAttendanceModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<CheckProfessorAttendanceModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}