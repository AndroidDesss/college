package com.desss.collegeproduct.module.professorSubModule.schedule.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.professorSubModule.report.model.ProfessorStudentReportModel
import com.desss.collegeproduct.module.professorSubModule.schedule.model.PostScheduleModel
import com.desss.collegeproduct.module.professorSubModule.schedule.model.UpdateScheduleModel
import com.desss.collegeproduct.module.professorSubModule.schedule.model.ViewScheduleModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ScheduleRepository {

    fun getDropDownValues(
        activity: Activity,
        action: String,
        table: String,
        userId: String
    ): LiveData<CommonResponseModel<ProfessorStudentReportModel>> {
        val data: MutableLiveData<CommonResponseModel<ProfessorStudentReportModel>> =
            MutableLiveData<CommonResponseModel<ProfessorStudentReportModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<ProfessorStudentReportModel>?>? =
            apiService?.getProfessorDepartment(action, table, userId)
        call?.enqueue(object : Callback<CommonResponseModel<ProfessorStudentReportModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<ProfessorStudentReportModel>?>,
                response: Response<CommonResponseModel<ProfessorStudentReportModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<ProfessorStudentReportModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }

    fun getPostScheduleMessage(
        activity: Activity,
        action: String,
        userId: String,
        degree: String,
        department: String,
        section: String,
        semester: String,
        date: String,
        fromTime: String,
        toTime: String,
        scheduleMessage: String
    ): LiveData<CommonResponseModel<PostScheduleModel>> {
        val data: MutableLiveData<CommonResponseModel<PostScheduleModel>> =
            MutableLiveData<CommonResponseModel<PostScheduleModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<PostScheduleModel>?>? = apiService?.getPostSchedule(
            action,
            userId,
            degree,
            department,
            section,
            semester,
            date,
            fromTime,
            toTime,
            scheduleMessage
        )
        call?.enqueue(object : Callback<CommonResponseModel<PostScheduleModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<PostScheduleModel>?>,
                response: Response<CommonResponseModel<PostScheduleModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<PostScheduleModel>?>,
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
        userId: String
    ): LiveData<CommonResponseModel<ViewScheduleModel>> {
        val data: MutableLiveData<CommonResponseModel<ViewScheduleModel>> =
            MutableLiveData<CommonResponseModel<ViewScheduleModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<ViewScheduleModel>?>? =
            apiService?.getViewSchedule(action, table, userId)
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

    fun updateScheduleValues(
        activity: Activity,
        action: String,
        table: String,
        scheduleId: String,
        fromTime: String,
        toTime: String,
        notes: String
    ): LiveData<CommonResponseModel<UpdateScheduleModel>> {
        val data: MutableLiveData<CommonResponseModel<UpdateScheduleModel>> =
            MutableLiveData<CommonResponseModel<UpdateScheduleModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<UpdateScheduleModel>?>? =
            apiService?.getUpdateSchedule(action, table, scheduleId, fromTime, toTime, notes)
        call?.enqueue(object : Callback<CommonResponseModel<UpdateScheduleModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<UpdateScheduleModel>?>,
                response: Response<CommonResponseModel<UpdateScheduleModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<UpdateScheduleModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}