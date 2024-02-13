package com.desss.collegeproduct.module.admission.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.admission.model.AdmissionModel
import com.desss.collegeproduct.module.admission.model.CourseModel
import com.desss.collegeproduct.module.admission.model.DegreeModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AdmissionRepository {

    fun getDegrees(
        activity: Activity,
        action: String,
        table: String,
        status: String,
        deleted: String
    ): LiveData<CommonResponseModel<DegreeModel>> {
        val data: MutableLiveData<CommonResponseModel<DegreeModel>> =
            MutableLiveData<CommonResponseModel<DegreeModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<DegreeModel>?>? =
            apiService?.managementDegrees(action, table, status,deleted)
        call?.enqueue(object : Callback<CommonResponseModel<DegreeModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<DegreeModel>?>,
                response: Response<CommonResponseModel<DegreeModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<DegreeModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }

    fun getCourses(
        activity: Activity,
        action: String,
        degreeId: String
    ): LiveData<CommonResponseModel<CourseModel>> {
        val data: MutableLiveData<CommonResponseModel<CourseModel>> =
            MutableLiveData<CommonResponseModel<CourseModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<CourseModel>?>? = apiService?.managementCourses(toRequestBody(action), toRequestBody(degreeId))
        call?.enqueue(object : Callback<CommonResponseModel<CourseModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<CourseModel>?>,
                response: Response<CommonResponseModel<CourseModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<CourseModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }

    fun postAmsDetails(
        activity: Activity,
        action: String,
        amsId: String,
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        alterPhoneNumber: String
    ): LiveData<CommonResponseModel<AdmissionModel>> {
        val data: MutableLiveData<CommonResponseModel<AdmissionModel>> =
            MutableLiveData<CommonResponseModel<AdmissionModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<AdmissionModel>?>? = apiService?.postAdmissionDetails(toRequestBody(action), toRequestBody(amsId), toRequestBody(firstName), toRequestBody(lastName), toRequestBody(email), toRequestBody(phoneNumber), toRequestBody(alterPhoneNumber))
        call?.enqueue(object : Callback<CommonResponseModel<AdmissionModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<AdmissionModel>?>,
                response: Response<CommonResponseModel<AdmissionModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<AdmissionModel>?>,
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