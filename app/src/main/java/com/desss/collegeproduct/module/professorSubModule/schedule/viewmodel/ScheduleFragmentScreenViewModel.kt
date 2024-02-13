package com.desss.collegeproduct.module.professorSubModule.schedule.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.professorSubModule.report.model.ProfessorStudentReportModel
import com.desss.collegeproduct.module.professorSubModule.schedule.model.PostScheduleModel
import com.desss.collegeproduct.module.professorSubModule.schedule.model.UpdateScheduleModel
import com.desss.collegeproduct.module.professorSubModule.schedule.model.ViewScheduleModel
import com.desss.collegeproduct.module.professorSubModule.schedule.repository.ScheduleRepository

@SuppressLint("StaticFieldLeak")
class ScheduleFragmentScreenViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {

    private var dropDownValuesDataObservable: LiveData<CommonResponseModel<ProfessorStudentReportModel>>? =
        null

    private var postScheduleMessageDataObservable: LiveData<CommonResponseModel<PostScheduleModel>>? =
        null

    private var getViewScheduleMessageDataObservable: LiveData<CommonResponseModel<ViewScheduleModel>>? =
        null

    private var getUpdateScheduleMessageDataObservable: LiveData<CommonResponseModel<UpdateScheduleModel>>? =
        null

    fun callDropDownValuesApi(activity: Activity, action: String, table: String, userId: String) {
        dropDownValuesDataObservable =
            ScheduleRepository.getDropDownValues(activity, action, table, userId)
    }

    fun getDropDownValuesData(): LiveData<CommonResponseModel<ProfessorStudentReportModel>>? {
        return dropDownValuesDataObservable
    }

    fun postScheduleMessage(
        activity: Activity,
        action: String,
        userId: String,
        degree: String,
        department: String,
        section: String,
        semester: String,
        date: String,
        fromTime: String,
        toTime: String,
        scheduleMessage: String
    ) {
        postScheduleMessageDataObservable = ScheduleRepository.getPostScheduleMessage(
            activity,
            action,
            userId,
            degree,
            department,
            section,
            semester,
            date,
            fromTime,
            toTime,
            scheduleMessage
        )
    }

    fun getPostScheduleMessageData(): LiveData<CommonResponseModel<PostScheduleModel>>? {
        return postScheduleMessageDataObservable
    }

    fun callScheduleApi(activity: Activity, action: String, table: String, userId: String) {
        getViewScheduleMessageDataObservable =
            ScheduleRepository.getScheduleValues(activity, action, table, userId)
    }

    fun getScheduleApiValuesData(): LiveData<CommonResponseModel<ViewScheduleModel>>? {
        return getViewScheduleMessageDataObservable
    }

    fun updateScheduleApi(
        activity: Activity,
        action: String,
        table: String,
        scheduleId: String,
        fromTime: String,
        toTime: String,
        notes: String
    ) {
        getUpdateScheduleMessageDataObservable = ScheduleRepository.updateScheduleValues(
            activity,
            action,
            table,
            scheduleId,
            fromTime,
            toTime,
            notes
        )
    }

    fun getUpdateScheduleApiValuesData(): LiveData<CommonResponseModel<UpdateScheduleModel>>? {
        return getUpdateScheduleMessageDataObservable
    }
}