package com.desss.collegeproduct.module.studentSubModule.examTimeTable.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.module.studentSubModule.examTimeTable.model.ExamTimeTableModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ExamTimeTableRepository {

    fun getExamTimeTable(
        activity: Activity,
        action: String,
        table: String,
        degree: String,
        course: String,
        semester: String
    ): LiveData<CommonResponseModel<ExamTimeTableModel>> {
        val data: MutableLiveData<CommonResponseModel<ExamTimeTableModel>> =
            MutableLiveData<CommonResponseModel<ExamTimeTableModel>>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<CommonResponseModel<ExamTimeTableModel>?>? =
            apiService?.studentExamTimeTable(action, table, degree, course, semester)
        call?.enqueue(object : Callback<CommonResponseModel<ExamTimeTableModel>?> {
            override fun onResponse(
                call: Call<CommonResponseModel<ExamTimeTableModel>?>,
                response: Response<CommonResponseModel<ExamTimeTableModel>?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<CommonResponseModel<ExamTimeTableModel>?>,
                t: Throwable
            ) {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
        return data
    }
}