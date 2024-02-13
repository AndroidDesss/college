package com.desss.collegeproduct.module.studentSubModule.notes.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.studentSubModule.notes.model.NotesModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object NotesRepository {
    fun getNotes(
        activity: Activity,
        action: String,
        degree: String,
        course: String,
        semester: String,
        section: String
    ): LiveData<CommonResponseModel<NotesModel>> {
        val data: MutableLiveData<CommonResponseModel<NotesModel>> =
            MutableLiveData<CommonResponseModel<NotesModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<NotesModel>?>? =
            apiService?.studentNotes(action, degree, course, semester, section)
        call?.enqueue(object : Callback<CommonResponseModel<NotesModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<NotesModel>?>,
                response: Response<CommonResponseModel<NotesModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<CommonResponseModel<NotesModel>?>, t: Throwable) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}