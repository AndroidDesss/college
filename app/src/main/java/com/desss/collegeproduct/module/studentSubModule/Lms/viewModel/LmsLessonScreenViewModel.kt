package com.desss.collegeproduct.module.studentSubModule.Lms.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.studentSubModule.Lms.model.LmsModel
import com.desss.collegeproduct.module.studentSubModule.Lms.repository.LmsRepository

@SuppressLint("StaticFieldLeak")
class LmsLessonScreenViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {

    private var lmsLessonDataObservable: LiveData<CommonResponseModel<LmsModel>>? = null

    private var lmsSingleLessonDataObservable: LiveData<CommonResponseModel<LmsModel>>? = null

    fun callLmsLessonApi(activity: Activity, action: String, degree: String, department: String, semester: String) {
        lmsLessonDataObservable = LmsRepository.getLessonData(activity, action, degree, department,semester)
    }
    fun getLmsLessonData(): LiveData<CommonResponseModel<LmsModel>>? {
        return lmsLessonDataObservable
    }

    fun callLmsSingleLessonApi(activity: Activity, action: String, degree: String, department: String, semester: String, lessonId: String) {
        lmsSingleLessonDataObservable = LmsRepository.getSingleLessonData(activity, action, degree, department,semester,lessonId)
    }
    fun getLmsSingleLessonData(): LiveData<CommonResponseModel<LmsModel>>? {
        return lmsSingleLessonDataObservable
    }

}