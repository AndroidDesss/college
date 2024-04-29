package com.desss.collegeproduct.module.studentSubModule.Lms.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.studentSubModule.Lms.model.LmsDurationModel
import com.desss.collegeproduct.module.studentSubModule.Lms.model.LmsModel
import com.desss.collegeproduct.module.studentSubModule.Lms.model.PostLmsDurationModel
import com.desss.collegeproduct.module.studentSubModule.Lms.model.UpdateLmsExamModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
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

    fun getLmsDurationData(
        activity: Activity,
        action: String,
        userId: String,
        lmsId: String
    ): LiveData<CommonResponseModel<LmsDurationModel>> {
        val data: MutableLiveData<CommonResponseModel<LmsDurationModel>> =
            MutableLiveData<CommonResponseModel<LmsDurationModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<LmsDurationModel>?>? =
            apiService?.lmsDurationData(action, userId, lmsId)
        call?.enqueue(object : Callback<CommonResponseModel<LmsDurationModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<LmsDurationModel>?>,
                response: Response<CommonResponseModel<LmsDurationModel>?>
            ) {
                data.value = response.body()
            }
            override fun onFailure(call: Call<CommonResponseModel<LmsDurationModel>?>, t: Throwable) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }

    fun postLmsDurationData(
        activity: Activity,
        action: String,
        userId: String,
        lmsId: String,
        lastWatchedSeconds: String,
        customDuration: String
    ): LiveData<CommonResponseModel<PostLmsDurationModel>> {
        val data: MutableLiveData<CommonResponseModel<PostLmsDurationModel>> =
            MutableLiveData<CommonResponseModel<PostLmsDurationModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<PostLmsDurationModel>?>? = apiService?.postDuration(toRequestBody(action), toRequestBody(userId), toRequestBody(lmsId), toRequestBody(lastWatchedSeconds), toRequestBody(customDuration))
        call?.enqueue(object : Callback<CommonResponseModel<PostLmsDurationModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<PostLmsDurationModel>?>,
                response: Response<CommonResponseModel<PostLmsDurationModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<PostLmsDurationModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }

    fun postLmsExamData(
        activity: Activity,
        action: String,
        userId: String,
        lmsId: String,
        questionCount: String,
        answerCount: String,
        questionAnswer: String
    ): LiveData<CommonResponseModel<UpdateLmsExamModel>> {
        val data: MutableLiveData<CommonResponseModel<UpdateLmsExamModel>> =
            MutableLiveData<CommonResponseModel<UpdateLmsExamModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<UpdateLmsExamModel>?>? = apiService?.postExamAnswers(toRequestBody(action), toRequestBody(userId), toRequestBody(lmsId), toRequestBody(questionCount), toRequestBody(answerCount), toRequestBody(questionAnswer))
        call?.enqueue(object : Callback<CommonResponseModel<UpdateLmsExamModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<UpdateLmsExamModel>?>,
                response: Response<CommonResponseModel<UpdateLmsExamModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<UpdateLmsExamModel>?>,
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