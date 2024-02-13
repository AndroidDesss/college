package com.desss.collegeproduct.module.admission.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.admission.model.AdmissionModel
import com.desss.collegeproduct.module.admission.model.CourseModel
import com.desss.collegeproduct.module.admission.model.DegreeModel
import com.desss.collegeproduct.module.admission.repository.AdmissionRepository

@SuppressLint("StaticFieldLeak")
class AdmissionViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {

    private var degreeDataObservable: LiveData<CommonResponseModel<DegreeModel>>? = null

    private var courseDataObservable: LiveData<CommonResponseModel<CourseModel>>? = null

    private var applicationFormDataObservable: LiveData<CommonResponseModel<AdmissionModel>>? = null

    fun callDegreeApi(
        activity: Activity,
        action: String,
        table: String,
        status: String,
        deleted: String
    ) {
        degreeDataObservable = AdmissionRepository.getDegrees(activity, action, table, status,deleted)
    }

    fun getDegreeDataObservable(): LiveData<CommonResponseModel<DegreeModel>>? {
        return degreeDataObservable
    }

    fun callCourseApi(
        activity: Activity,
        action: String,
        degreeId: String
    ) {
        courseDataObservable = AdmissionRepository.getCourses(activity, action, degreeId)
    }

    fun getCourseDataObservable(): LiveData<CommonResponseModel<CourseModel>>? {
        return courseDataObservable
    }

    fun submitAmsDetails(
        activity: Activity,
        action: String,
        amsId: String,
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        alterPhoneNumber: String
    ) {
        applicationFormDataObservable = AdmissionRepository.postAmsDetails(activity, action, amsId,firstName,lastName,email,phoneNumber,alterPhoneNumber)
    }

    fun getAmsDataObservable(): LiveData<CommonResponseModel<AdmissionModel>>? {
        return applicationFormDataObservable
    }

}