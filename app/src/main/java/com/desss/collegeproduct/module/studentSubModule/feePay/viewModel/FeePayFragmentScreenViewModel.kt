package com.desss.collegeproduct.module.studentSubModule.feePay.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.studentSubModule.feePay.model.FeePayModel
import com.desss.collegeproduct.module.studentSubModule.feePay.repository.FeePayRepository

@SuppressLint("StaticFieldLeak")
class FeePayFragmentScreenViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {

    private var feePayDataObservable: LiveData<CommonResponseModel<FeePayModel>>? = null

    fun callFeePayApi(activity: Activity, action: String, table: String, userId: String) {
        feePayDataObservable = FeePayRepository.getFeePay(activity, action, table, userId)
    }

    fun getFeePayData(): LiveData<CommonResponseModel<FeePayModel>>? {
        return feePayDataObservable
    }
}