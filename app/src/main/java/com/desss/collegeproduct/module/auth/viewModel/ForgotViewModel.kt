package com.desss.collegeproduct.module.auth.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.auth.model.ForgotModel
import com.desss.collegeproduct.module.auth.repository.ForgotPasswordRepository

@SuppressLint("StaticFieldLeak")
class ForgotViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {

    private var otpDataObservable: LiveData<CommonResponseModel<ForgotModel>>? = null

    fun callOtpApi(activity: Activity, action: String, email: String) {
        otpDataObservable = ForgotPasswordRepository.getOtp(activity, action, email)
    }

    fun getOtpApi(): LiveData<CommonResponseModel<ForgotModel>>? {
        return otpDataObservable
    }
}