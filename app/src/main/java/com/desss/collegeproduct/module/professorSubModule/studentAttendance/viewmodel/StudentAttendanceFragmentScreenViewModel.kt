package com.desss.collegeproduct.module.professorSubModule.studentAttendance.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.professorSubModule.report.model.ProfessorStudentReportModel
import com.desss.collegeproduct.module.professorSubModule.report.model.StudentListBasedModel
import com.desss.collegeproduct.module.professorSubModule.studentAttendance.model.AddStudentAttendanceModel
import com.desss.collegeproduct.module.professorSubModule.studentAttendance.model.StudentCountModel
import com.desss.collegeproduct.module.professorSubModule.studentAttendance.repository.StudentAttendanceRepository

@SuppressLint("StaticFieldLeak")
class StudentAttendanceFragmentScreenViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {
    private var dropDownValuesDataObservable: LiveData<CommonResponseModel<ProfessorStudentReportModel>>? =
        null

    private var studentsCountListDataObservable: LiveData<CommonResponseModel<StudentCountModel>>? =
        null

    private var studentsListDataObservable: LiveData<CommonResponseModel<StudentListBasedModel>>? =
        null

    private var studentsAttendanceDataObservable: LiveData<CommonResponseModel<AddStudentAttendanceModel>>? =
        null

    fun callDropDownValuesApi(activity: Activity, action: String, table: String, userId: String) {
        dropDownValuesDataObservable =
            StudentAttendanceRepository.getDropDownValues(activity, action, table, userId)
    }

    fun getDropDownValuesData(): LiveData<CommonResponseModel<ProfessorStudentReportModel>>? {
        return dropDownValuesDataObservable
    }

    fun callStudentCountListApi(
        activity: Activity,
        action: String,
        department: String,
        section: String,
        semester: String,
        degree: String
    ) {
        studentsCountListDataObservable = StudentAttendanceRepository.getStudentCountList(
            activity,
            action,
            department,
            section,
            semester,
            degree
        )
    }

    fun getStudentCountListApiValuesData(): LiveData<CommonResponseModel<StudentCountModel>>? {
        return studentsCountListDataObservable
    }

    fun callStudentListApi(
        activity: Activity,
        action: String,
        degree: String,
        department: String,
        semester: String,
        section: String
    ) {
        studentsListDataObservable = StudentAttendanceRepository.getStudentListValues(
            activity,
            action,
            degree,
            department,
            semester,
            section
        )
    }

    fun getStudentListData(): LiveData<CommonResponseModel<StudentListBasedModel>>? {
        return studentsListDataObservable
    }

    fun callMarkStudentAttendanceApi(
        activity: Activity,
        action: String,
        professorUserId: String,
        studentId: String
    ) {
        studentsAttendanceDataObservable =
            StudentAttendanceRepository.getStudentAttendanceListValues(
                activity,
                action,
                professorUserId,
                studentId
            )
    }

    fun getMarkStudentAttendanceApiData(): LiveData<CommonResponseModel<AddStudentAttendanceModel>>? {
        return studentsAttendanceDataObservable
    }
}