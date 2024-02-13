package com.desss.collegeproduct.module.professorSubModule.department.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.professorSubModule.department.model.DepartmentModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DepartmentRepository {
    fun getDepartment(
        activity: Activity,
        action: String,
        table: String,
        userId: String
    ): LiveData<CommonResponseModel<DepartmentModel>> {
        val data: MutableLiveData<CommonResponseModel<DepartmentModel>> =
            MutableLiveData<CommonResponseModel<DepartmentModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<DepartmentModel>?>? =
            apiService?.professorDepartment(action, table, userId)
        call?.enqueue(object : Callback<CommonResponseModel<DepartmentModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<DepartmentModel>?>,
                response: Response<CommonResponseModel<DepartmentModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<DepartmentModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}