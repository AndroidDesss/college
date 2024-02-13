package com.desss.collegeproduct.module.managementSubModule.studentDetail.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.managementSubModule.studentDetail.model.DegreeDepartmentSectionModel
import com.desss.collegeproduct.module.managementSubModule.studentDetail.model.StudentListManagementBasedModel
import com.desss.collegeproduct.module.managementSubModule.studentDetail.repository.StudentDetailRepository
import com.desss.collegeproduct.module.professorSubModule.report.model.StudentListBasedModel
import com.desss.collegeproduct.module.professorSubModule.report.repository.ReportRepository

@SuppressLint("StaticFieldLeak")
class StudentDetailScreenViewModel(application: Application, val activity: Activity): AndroidViewModel(application) {

    private var dropDownValuesDataObservable: LiveData<CommonResponseModel<DegreeDepartmentSectionModel>>? = null

    private var studentsListDataObservable: LiveData<CommonResponseModel<StudentListManagementBasedModel>>? = null

    fun callDropDownValuesApi(activity: Activity, action:String, table:String)
    {
        dropDownValuesDataObservable = StudentDetailRepository.getDropDownValues(activity, action,table)
    }

    fun getDropDownValuesData(): LiveData<CommonResponseModel<DegreeDepartmentSectionModel>>? {
        return dropDownValuesDataObservable
    }

    fun callStudentListApi(activity: Activity, action:String, degree:String, department:String, semester:String, section:String)
    {
        studentsListDataObservable = StudentDetailRepository.getStudentListValues(activity, action,degree,department,semester,section)
    }

    fun getStudentListData(): LiveData<CommonResponseModel<StudentListManagementBasedModel>>? {
        return studentsListDataObservable
    }
}