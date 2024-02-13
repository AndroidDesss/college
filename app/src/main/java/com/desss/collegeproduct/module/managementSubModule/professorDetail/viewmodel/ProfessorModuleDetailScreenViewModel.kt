package com.desss.collegeproduct.module.managementSubModule.professorDetail.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.managementSubModule.professorDetail.model.TotalProfessorModel
import com.desss.collegeproduct.module.managementSubModule.professorDetail.repository.ProfessorListRepository

@SuppressLint("StaticFieldLeak")
class ProfessorModuleDetailScreenViewModel(application: Application, val activity: Activity): AndroidViewModel(application) {

    private var professorListDataObservable: LiveData<CommonResponseModel<TotalProfessorModel>>? = null

    fun callProfessorListApi(activity: Activity, action: String, table: String, rollId: String) {
        professorListDataObservable =
            ProfessorListRepository.getProfessorList(activity, action, table, rollId)
    }

    fun getProfessorListData(): LiveData<CommonResponseModel<TotalProfessorModel>>? {
        return professorListDataObservable
    }
}