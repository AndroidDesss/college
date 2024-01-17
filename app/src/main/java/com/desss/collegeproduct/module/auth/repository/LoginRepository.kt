package com.desss.collegeproduct.module.auth.repository

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desss.collegeproduct.module.auth.model.LoginModel
import com.desss.collegeproduct.repository.service.ApiServices
import com.desss.collegeproduct.repository.service.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object LoginRepository {

    fun submitLogin(activity: Activity,action:String,table:String,userId:String,password:String): LiveData<LoginModel> {
        val data: MutableLiveData<LoginModel> = MutableLiveData<LoginModel>()
        val apiService = ApiClient.client?.create(ApiServices::class.java)
        val call: Call<LoginModel?>? = apiService?.loginSubmit(action,table,userId,password)
        call?.enqueue(object : Callback<LoginModel?> {
            override fun onResponse(call: Call<LoginModel?>, response: Response<LoginModel?>) {
                data.value = response.body()
            }
            override fun onFailure(call: Call<LoginModel?>, t: Throwable) {
                Log.d("errorResponse", t.message.toString())
//                Utils.hideBusyAnimation(activity)
            }
        })
        return data
    }
}