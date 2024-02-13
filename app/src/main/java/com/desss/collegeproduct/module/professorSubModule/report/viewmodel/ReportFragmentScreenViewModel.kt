package com.desss.collegeproduct.module.professorSubModule.report.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.professorSubModule.report.model.AddReportModel
import com.desss.collegeproduct.module.professorSubModule.report.model.ProfessorStudentReportModel
import com.desss.collegeproduct.module.professorSubModule.report.model.StudentListBasedModel
import com.desss.collegeproduct.module.professorSubModule.report.repository.ReportRepository

@SuppressLint("StaticFieldLeak")
class ReportFragmentScreenViewModel(application: Application, val activity: Activity): AndroidViewModel(application)
{
    private var dropDownValuesDataObservable: LiveData<CommonResponseModel<ProfessorStudentReportModel>>? = null

    private var studentsListDataObservable: LiveData<CommonResponseModel<StudentListBasedModel>>? = null

    private var postReportListDataObservable: LiveData<CommonResponseModel<AddReportModel>>? = null


    fun callDropDownValuesApi(activity: Activity, action:String, table:String, userId:String)
    {
        dropDownValuesDataObservable = ReportRepository.getDropDownValues(activity, action,table,userId)
    }

    fun getDropDownValuesData(): LiveData<CommonResponseModel<ProfessorStudentReportModel>>? {
        return dropDownValuesDataObservable
    }

    fun callStudentListApi(activity: Activity, action:String, degree:String, department:String, semester:String, section:String)
    {
        studentsListDataObservable = ReportRepository.getStudentListValues(activity, action,degree,department,semester,section)
    }

    fun getStudentListData(): LiveData<CommonResponseModel<StudentListBasedModel>>? {
        return studentsListDataObservable
    }

    fun postReportsApi(activity: Activity, action:String, professorId:String, studentId:String, content:String, regNo:String, name:String)
    {
        postReportListDataObservable = ReportRepository.getAddReportListValues(activity, action,professorId,studentId,content,regNo,name)
    }

    fun getPostReports(): LiveData<CommonResponseModel<AddReportModel>>? {
        return postReportListDataObservable
    }

}