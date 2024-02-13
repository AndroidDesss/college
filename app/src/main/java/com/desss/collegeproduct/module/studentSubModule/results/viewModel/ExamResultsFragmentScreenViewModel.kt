package com.desss.collegeproduct.module.studentSubModule.results.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.studentSubModule.results.model.ResultsModel
import com.desss.collegeproduct.module.studentSubModule.results.repository.ResultsRepository

@SuppressLint("StaticFieldLeak")
class ExamResultsFragmentScreenViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {

    private var resultsDataObservable: LiveData<CommonResponseModel<ResultsModel>>? = null

    fun callResultsApi(activity: Activity, action: String, table: String, timeTableId: String, userId: String) {
        resultsDataObservable = ResultsRepository.getResults(activity, action, table, timeTableId, userId)
    }

    fun getResultsData(): LiveData<CommonResponseModel<ResultsModel>>? {
        return resultsDataObservable
    }
}