package com.desss.collegeproduct.module.managementSubModule.studentDetail.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.managementSubModule.studentDetail.model.DegreeDepartmentSectionModel
import com.desss.collegeproduct.module.managementSubModule.studentDetail.model.StudentListManagementBasedModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object StudentDetailRepository {

    fun getDropDownValues(
        activity: Activity,
        action: String,
        table: String,
    ): LiveData<CommonResponseModel<DegreeDepartmentSectionModel>> {
        val data: MutableLiveData<CommonResponseModel<DegreeDepartmentSectionModel>> =
            MutableLiveData<CommonResponseModel<DegreeDepartmentSectionModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<DegreeDepartmentSectionModel>?>? =
            apiService?.getStudentSideDepartmentMangement(action, table)
        call?.enqueue(object : Callback<CommonResponseModel<DegreeDepartmentSectionModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<DegreeDepartmentSectionModel>?>,
                response: Response<CommonResponseModel<DegreeDepartmentSectionModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<DegreeDepartmentSectionModel>?>,
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
    ): LiveData<CommonResponseModel<StudentListManagementBasedModel>> {
        val data: MutableLiveData<CommonResponseModel<StudentListManagementBasedModel>> =
            MutableLiveData<CommonResponseModel<StudentListManagementBasedModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<StudentListManagementBasedModel>?>? =
            apiService?.getStudentListByManagement(action, degree, department, semester, section)
        call?.enqueue(object : Callback<CommonResponseModel<StudentListManagementBasedModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<StudentListManagementBasedModel>?>,
                response: Response<CommonResponseModel<StudentListManagementBasedModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<StudentListManagementBasedModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}