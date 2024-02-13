package com.desss.collegeproduct.module.professorSubModule.report.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.professorSubModule.report.model.ViewReportsModel
import com.desss.collegeproduct.module.professorSubModule.report.repository.ViewReportRepository

@SuppressLint("StaticFieldLeak")
class ViewReportFragmentScreenViewModel(application: Application, val activity: Activity): AndroidViewModel(application)
{
    private var reportsValuesDataObservable: LiveData<CommonResponseModel<ViewReportsModel>>? = null
    fun callReportsValuesApi(activity: Activity, action:String, table:String, proUserId:String, status:String)
    {
        reportsValuesDataObservable = ViewReportRepository.getReportsValues(activity, action,table,proUserId,status)
    }

    fun getReportsListData(): LiveData<CommonResponseModel<ViewReportsModel>>? {
        return reportsValuesDataObservable
    }
}