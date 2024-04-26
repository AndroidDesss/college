package com.desss.collegeproduct.module.studentSubModule.Lms.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.studentSubModule.Lms.model.LmsModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object LmsRepository {

    fun getLessonData(
        activity: Activity,
        action: String,
        degree: String,
        department: String,
        semester: String
    ): LiveData<CommonResponseModel<LmsModel>> {
        val data: MutableLiveData<CommonResponseModel<LmsModel>> =
            MutableLiveData<CommonResponseModel<LmsModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<LmsModel>?>? =
            apiService?.lmsLessonData(action, degree, department,semester)
        call?.enqueue(object : Callback<CommonResponseModel<LmsModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<LmsModel>?>,
                response: Response<CommonResponseModel<LmsModel>?>
            ) {
                data.value = response.body()
            }
            override fun onFailure(call: Call<CommonResponseModel<LmsModel>?>, t: Throwable) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }

    fun getSingleLessonData(
        activity: Activity,
        action: String,
        degree: String,
        department: String,
        semester: String,
        lessonId: String
    ): LiveData<CommonResponseModel<LmsModel>> {
        val data: MutableLiveData<CommonResponseModel<LmsModel>> =
            MutableLiveData<CommonResponseModel<LmsModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<LmsModel>?>? =
            apiService?.lmsSingleLessonData(action, degree, department,semester,lessonId)
        call?.enqueue(object : Callback<CommonResponseModel<LmsModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<LmsModel>?>,
                response: Response<CommonResponseModel<LmsModel>?>
            ) {
                data.value = response.body()
            }
            override fun onFailure(call: Call<CommonResponseModel<LmsModel>?>, t: Throwable) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }

}