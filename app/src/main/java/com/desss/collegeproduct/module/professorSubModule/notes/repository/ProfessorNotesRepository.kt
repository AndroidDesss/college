package com.desss.collegeproduct.module.professorSubModule.notes.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.professorSubModule.notes.model.UploadSuccessModel
import com.desss.collegeproduct.module.professorSubModule.report.model.ProfessorStudentReportModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ProfessorNotesRepository {
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

    fun uploadPdfFileWithMultiPart(
        activity: Activity,
        action: String,
        professorId: String,
        degree: String,
        department: String,
        section: String,
        semester: String,
        name: String,
        pdfFile: MultipartBody.Part
    ): LiveData<CommonResponseModel<UploadSuccessModel>> {
        val data: MutableLiveData<CommonResponseModel<UploadSuccessModel>> =
            MutableLiveData<CommonResponseModel<UploadSuccessModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<UploadSuccessModel>?>? =
            apiService?.uploadPdfValuesWithBodyMultiPart(
                toRequestBody(action),
                toRequestBody(professorId),
                toRequestBody(degree),
                toRequestBody(department),
                toRequestBody(section),
                toRequestBody(semester),
                toRequestBody(name),
                pdfFile
            )
        call?.enqueue(object : Callback<CommonResponseModel<UploadSuccessModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<UploadSuccessModel>?>,
                response: Response<CommonResponseModel<UploadSuccessModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<UploadSuccessModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }

    private fun toRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }
}