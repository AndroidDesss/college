package com.desss.collegeproduct.module.studentSubModule.attendance.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.studentSubModule.attendance.model.StudentAttendanceModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AttendanceRepository {
    fun getAttendance(
        activity: Activity,
        action: String,
        userId: String
    ): LiveData<CommonResponseModel<StudentAttendanceModel>> {
        val data: MutableLiveData<CommonResponseModel<StudentAttendanceModel>> =
            MutableLiveData<CommonResponseModel<StudentAttendanceModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<StudentAttendanceModel>?>? =
            apiService?.studentAttendance(action, userId)
        call?.enqueue(object : Callback<CommonResponseModel<StudentAttendanceModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<StudentAttendanceModel>?>,
                response: Response<CommonResponseModel<StudentAttendanceModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<StudentAttendanceModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}