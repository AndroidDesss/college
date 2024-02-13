package com.desss.collegeproduct.module.managementSubModule.professorDetail.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.commonDashBoardFragment.profile.model.ProfileModel
import com.desss.collegeproduct.module.commonDashBoardFragment.profile.repository.ProfileRepository
import com.desss.collegeproduct.module.managementSubModule.professorDetail.repository.ProfessorListRepository
import com.desss.collegeproduct.module.professorSubModule.professorAttendance.model.ProfessorCountModel
import com.desss.collegeproduct.module.professorSubModule.professorAttendance.repository.ProfessorAttendanceRepository
import com.desss.collegeproduct.module.professorSubModule.schedule.model.ViewScheduleModel

@SuppressLint("StaticFieldLeak")
class ProfessorSubModuleDetailScreenViewModel(application: Application, val activity: Activity): AndroidViewModel(application) {

    private var professorProfileDataObservable: LiveData<CommonResponseModel<ProfileModel>>? = null

    private var professorCountDataObservable: LiveData<CommonResponseModel<ProfessorCountModel>>? =
        null

    private var getViewScheduleMessageDataObservable: LiveData<CommonResponseModel<ViewScheduleModel>>? =
        null

    fun callStudentProfileApi(activity: Activity, action: String, table: String, id: String) {
        professorProfileDataObservable =
            ProfileRepository.studentProfileData(activity, action, table, id)
    }

    fun getStudentProfileData(): LiveData<CommonResponseModel<ProfileModel>>? {
        return professorProfileDataObservable
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

    fun callScheduleApi(activity: Activity, action: String, table: String, userId: String, date: String) {
        getViewScheduleMessageDataObservable =
            ProfessorListRepository.getScheduleValues(activity, action, table, userId,date)
    }

    fun getScheduleApiValuesData(): LiveData<CommonResponseModel<ViewScheduleModel>>? {
        return getViewScheduleMessageDataObservable
    }
}