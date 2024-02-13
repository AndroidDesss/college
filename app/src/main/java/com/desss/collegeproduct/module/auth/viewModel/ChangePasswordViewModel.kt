package com.desss.collegeproduct.module.auth.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.auth.model.ChangePasswordModel
import com.desss.collegeproduct.module.auth.repository.ChangePasswordRepository

@SuppressLint("StaticFieldLeak")
class ChangePasswordViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {

    private var changePasswordDataObservable: LiveData<CommonResponseModel<ChangePasswordModel>>? =
        null

    fun callChangePassword(activity: Activity, action: String, email: String, password: String) {
        changePasswordDataObservable =
            ChangePasswordRepository.getUpdatePassword(activity, action, email, password)
    }

    fun getChangePasswordValues(): LiveData<CommonResponseModel<ChangePasswordModel>>? {
        return changePasswordDataObservable
    }
}