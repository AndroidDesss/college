package com.desss.collegeproduct.module.studentSubModule.syllabus.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.studentSubModule.syllabus.model.SyllabusModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SyllabusRepository {
    fun getSyllabus(
        activity: Activity,
        action: String,
        table: String,
        degree: String,
        course: String,
        semester: String
    ): LiveData<CommonResponseModel<SyllabusModel>> {
        val data: MutableLiveData<CommonResponseModel<SyllabusModel>> =
            MutableLiveData<CommonResponseModel<SyllabusModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<SyllabusModel>?>? =
            apiService?.studentSyllabus(action, table, degree, course, semester)
        call?.enqueue(object : Callback<CommonResponseModel<SyllabusModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<SyllabusModel>?>,
                response: Response<CommonResponseModel<SyllabusModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<CommonResponseModel<SyllabusModel>?>, t: Throwable) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}