package com.desss.collegeproduct.module.professorSubModule.report.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.professorSubModule.report.model.AddReportModel
import com.desss.collegeproduct.module.professorSubModule.report.model.ProfessorStudentReportModel
import com.desss.collegeproduct.module.professorSubModule.report.model.StudentListBasedModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ReportRepository {
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

    fun getAddReportListValues(
        activity: Activity,
        action: String,
        professorId: String,
        studentId: String,
        content: String,
        regNo: String,
        name: String
    ): LiveData<CommonResponseModel<AddReportModel>> {
        val data: MutableLiveData<CommonResponseModel<AddReportModel>> =
            MutableLiveData<CommonResponseModel<AddReportModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<AddReportModel>?>? =
            apiService?.postAddReport(action, professorId, studentId, content, regNo, name)
        call?.enqueue(object : Callback<CommonResponseModel<AddReportModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<AddReportModel>?>,
                response: Response<CommonResponseModel<AddReportModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<CommonResponseModel<AddReportModel>?>, t: Throwable) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}