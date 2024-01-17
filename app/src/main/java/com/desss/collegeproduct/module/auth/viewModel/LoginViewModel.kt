package com.desss.collegeproduct.module.auth.viewModel


import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.module.auth.model.LoginModel
import com.desss.collegeproduct.module.auth.repository.LoginRepository

class LoginViewModel (application: Application, val activity: Activity) : AndroidViewModel(application)
{
    private var loginSubmitObservable: LiveData<LoginModel>? = null

    fun submitLogin(activity: Activity,action:String,table:String,userId:String,password:String)
    {
        loginSubmitObservable = LoginRepository.submitLogin(activity, action,table,userId,password)
    }

    fun submitLoginObservable(): LiveData<LoginModel>? {
        return loginSubmitObservable
    }
}