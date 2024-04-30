package com.desss.collegeproduct.module.studentSubModule.transport.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.studentSubModule.transport.model.TransportModel
import com.desss.collegeproduct.module.studentSubModule.transport.repository.TransportRepository

@SuppressLint("StaticFieldLeak")
class TransportViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {

    private var transportDataObservable: LiveData<CommonResponseModel<TransportModel>>? = null

    fun callTransportApi(activity: Activity, action: String, busNo: String) {
        transportDataObservable = TransportRepository.getTransportData(activity, action, busNo)
    }
    fun getLmsLessonData(): LiveData<CommonResponseModel<TransportModel>>? {
        return transportDataObservable
    }

}