package com.desss.collegeproduct.module.professorSubModule.department.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.professorSubModule.department.model.DepartmentModel
import com.desss.collegeproduct.module.professorSubModule.department.repository.DepartmentRepository

@SuppressLint("StaticFieldLeak")
class DepartmentFragmentScreenViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {

    private var departmentDataObservable: LiveData<CommonResponseModel<DepartmentModel>>? = null

    fun callDepartmentApi(activity: Activity, action: String, table: String, userId: String) {
        departmentDataObservable =
            DepartmentRepository.getDepartment(activity, action, table, userId)
    }

    fun getAttendanceData(): LiveData<CommonResponseModel<DepartmentModel>>? {
        return departmentDataObservable
    }
}