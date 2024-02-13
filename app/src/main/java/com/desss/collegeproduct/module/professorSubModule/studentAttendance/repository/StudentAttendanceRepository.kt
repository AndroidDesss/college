package com.desss.collegeproduct.module.professorSubModule.studentAttendance.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.professorSubModule.report.model.ProfessorStudentReportModel
import com.desss.collegeproduct.module.professorSubModule.report.model.StudentListBasedModel
import com.desss.collegeproduct.module.professorSubModule.studentAttendance.model.AddStudentAttendanceModel
import com.desss.collegeproduct.module.professorSubModule.studentAttendance.model.StudentCountModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object StudentAttendanceRepository {
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

    fun getStudentCountList(
        activity: Activity,
        action: String,
        department: String,
        section: String,
        semester: String,
        degree: String
    ): LiveData<CommonResponseModel<StudentCountModel>> {
        val data: MutableLiveData<CommonResponseModel<StudentCountModel>> =
            MutableLiveData<CommonResponseModel<StudentCountModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<StudentCountModel>?>? =
            apiService?.getStudentCountList(action, department, section, semester, degree)
        call?.enqueue(object : Callback<CommonResponseModel<StudentCountModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<StudentCountModel>?>,
                response: Response<CommonResponseModel<StudentCountModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<StudentCountModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }

    fun getStudentListValues(
        activity: Activity,
        action: String,
        degree: String,
        department: String,
        semester: String,
        section: String
    ): LiveData<CommonResponseModel<StudentListBasedModel>> {
        val data: MutableLiveData<CommonResponseModel<StudentListBasedModel>> =
            MutableLiveData<CommonResponseModel<StudentListBasedModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<StudentListBasedModel>?>? =
            apiService?.getStudentList(action, degree, department, semester, section)
        call?.enqueue(object : Callback<CommonResponseModel<StudentListBasedModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<StudentListBasedModel>?>,
                response: Response<CommonResponseModel<StudentListBasedModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<StudentListBasedModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }

    fun getStudentAttendanceListValues(
        activity: Activity,
        action: String,
        professorUserId: String,
        studentId: String
    ): LiveData<CommonResponseModel<AddStudentAttendanceModel>> {
        val data: MutableLiveData<CommonResponseModel<AddStudentAttendanceModel>> =
            MutableLiveData<CommonResponseModel<AddStudentAttendanceModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<AddStudentAttendanceModel>?>? =
            apiService?.getStudentAttendanceList(action, professorUserId, studentId)
        call?.enqueue(object : Callback<CommonResponseModel<AddStudentAttendanceModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<AddStudentAttendanceModel>?>,
                response: Response<CommonResponseModel<AddStudentAttendanceModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<AddStudentAttendanceModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}