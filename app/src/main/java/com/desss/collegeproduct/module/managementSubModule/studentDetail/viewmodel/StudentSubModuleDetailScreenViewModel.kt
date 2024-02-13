package com.desss.collegeproduct.module.managementSubModule.studentDetail.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.commonDashBoardFragment.profile.model.ProfileModel
import com.desss.collegeproduct.module.commonDashBoardFragment.profile.repository.ProfileRepository
import com.desss.collegeproduct.module.studentSubModule.attendance.model.StudentAttendanceModel
import com.desss.collegeproduct.module.studentSubModule.attendance.repository.AttendanceRepository
import com.desss.collegeproduct.module.studentSubModule.feePay.model.FeePayModel
import com.desss.collegeproduct.module.studentSubModule.feePay.repository.FeePayRepository
import com.desss.collegeproduct.module.studentSubModule.remarks.model.RemarksModel
import com.desss.collegeproduct.module.studentSubModule.remarks.repository.RemarksRepository

@SuppressLint("StaticFieldLeak")
class StudentSubModuleDetailScreenViewModel(application: Application, val activity: Activity): AndroidViewModel(application) {

    private var studentProfileDataObservable: LiveData<CommonResponseModel<ProfileModel>>? = null

    private var remarksDataObservable: LiveData<CommonResponseModel<RemarksModel>>? = null

    private var attendanceDataObservable: LiveData<CommonResponseModel<StudentAttendanceModel>>? =
        null

    private var feePayDataObservable: LiveData<CommonResponseModel<FeePayModel>>? = null

    fun callFeePayApi(activity: Activity, action: String, table: String, userId: String) {
        feePayDataObservable = FeePayRepository.getFeePay(activity, action, table, userId)
    }

    fun getFeePayData(): LiveData<CommonResponseModel<FeePayModel>>? {
        return feePayDataObservable
    }

    fun callAttendanceApi(activity: Activity, action: String, userId: String) {
        attendanceDataObservable = AttendanceRepository.getAttendance(activity, action, userId)
    }

    fun getAttendanceData(): LiveData<CommonResponseModel<StudentAttendanceModel>>? {
        return attendanceDataObservable
    }

    fun callRemarksApi(activity: Activity, action: String, table: String, userId: String) {
        remarksDataObservable = RemarksRepository.getRemarks(activity, action, table, userId)
    }

    fun getRemarksData(): LiveData<CommonResponseModel<RemarksModel>>? {
        return remarksDataObservable
    }

    fun callStudentProfileApi(activity: Activity, action: String, table: String, id: String) {
        studentProfileDataObservable =
            ProfileRepository.studentProfileData(activity, action, table, id)
    }

    fun getStudentProfileData(): LiveData<CommonResponseModel<ProfileModel>>? {
        return studentProfileDataObservable
    }

}