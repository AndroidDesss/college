package com.desss.collegeproduct.module.studentSubModule.attendance.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.studentSubModule.attendance.model.StudentAttendanceModel
import com.desss.collegeproduct.module.studentSubModule.attendance.repository.AttendanceRepository

@SuppressLint("StaticFieldLeak")
class AttendanceFragmentScreenViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {
    private var attendanceDataObservable: LiveData<CommonResponseModel<StudentAttendanceModel>>? =
        null

    fun callAttendanceApi(activity: Activity, action: String, userId: String) {
        attendanceDataObservable = AttendanceRepository.getAttendance(activity, action, userId)
    }

    fun getAttendanceData(): LiveData<CommonResponseModel<StudentAttendanceModel>>? {
        return attendanceDataObservable
    }
}