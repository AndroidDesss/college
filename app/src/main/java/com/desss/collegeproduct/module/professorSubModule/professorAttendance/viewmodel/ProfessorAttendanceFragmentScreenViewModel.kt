package com.desss.collegeproduct.module.professorSubModule.professorAttendance.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.professorSubModule.professorAttendance.model.CheckProfessorAttendanceModel
import com.desss.collegeproduct.module.professorSubModule.professorAttendance.model.ProfessorCountModel
import com.desss.collegeproduct.module.professorSubModule.professorAttendance.repository.ProfessorAttendanceRepository

@SuppressLint("StaticFieldLeak")
class ProfessorAttendanceFragmentScreenViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {


    private var checkAttendanceDataObservable: LiveData<CommonResponseModel<CheckProfessorAttendanceModel>>? =
        null

    private var professorCountDataObservable: LiveData<CommonResponseModel<ProfessorCountModel>>? =
        null

    private var markProfessorAttendanceDataObservable: LiveData<CommonResponseModel<CheckProfessorAttendanceModel>>? =
        null

    private var checkAttendanceAlreadyDataObservable: LiveData<CommonResponseModel<CheckProfessorAttendanceModel>>? =
        null

    fun callCheckProfessorAttendanceApi(activity: Activity, action: String, userId: String) {
        checkAttendanceDataObservable =
            ProfessorAttendanceRepository.getProfessorAttendanceApi(activity, action, userId)
    }

    fun getCheckProfessorAttendanceData(): LiveData<CommonResponseModel<CheckProfessorAttendanceModel>>? {
        return checkAttendanceDataObservable
    }

    fun callProfessorCountApi(
        activity: Activity,
        action: String,
        userId: String,
        month: String,
        year: String
    ) {
        professorCountDataObservable = ProfessorAttendanceRepository.getProfessorCountApi(
            activity,
            action,
            userId,
            month,
            year
        )
    }

    fun getProfessorCountData(): LiveData<CommonResponseModel<ProfessorCountModel>>? {
        return professorCountDataObservable
    }

    fun callMarkProfessorAttendanceApi(activity: Activity, action: String, userId: String) {
        markProfessorAttendanceDataObservable =
            ProfessorAttendanceRepository.getMarkProfessorAttendanceApi(activity, action, userId)
    }

    fun getMarkProfessorAttendanceData(): LiveData<CommonResponseModel<CheckProfessorAttendanceModel>>? {
        return markProfessorAttendanceDataObservable
    }

    fun callCheckProfessorAttendanceAlreadyMarkedApi(activity: Activity, action: String, userId: String) {
        checkAttendanceAlreadyDataObservable =
            ProfessorAttendanceRepository.getProfessorAttendanceApi(activity, action, userId)
    }

    fun getCheckProfessorAttendanceAlreadyMarkedData(): LiveData<CommonResponseModel<CheckProfessorAttendanceModel>>? {
        return checkAttendanceAlreadyDataObservable
    }
}