package com.desss.collegeproduct.module.studentSubModule.remarks.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.studentSubModule.remarks.model.RemarksModel
import com.desss.collegeproduct.module.studentSubModule.remarks.repository.RemarksRepository

@SuppressLint("StaticFieldLeak")
class RemarksFragmentScreenViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {
    private var remarksDataObservable: LiveData<CommonResponseModel<RemarksModel>>? = null

    fun callRemarksApi(activity: Activity, action: String, table: String, userId: String) {
        remarksDataObservable = RemarksRepository.getRemarks(activity, action, table, userId)
    }

    fun getRemarksData(): LiveData<CommonResponseModel<RemarksModel>>? {
        return remarksDataObservable
    }
}