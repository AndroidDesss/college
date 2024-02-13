package com.desss.collegeproduct.module.studentSubModule.syllabus.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.studentSubModule.syllabus.model.SyllabusModel
import com.desss.collegeproduct.module.studentSubModule.syllabus.repository.SyllabusRepository

@SuppressLint("StaticFieldLeak")
class SyllabusFragmentScreenViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {

    private var syllabusDataObservable: LiveData<CommonResponseModel<SyllabusModel>>? = null

    fun callSyllabusApi(
        activity: Activity,
        action: String,
        table: String,
        degree: String,
        course: String,
        semester: String
    ) {
        syllabusDataObservable =
            SyllabusRepository.getSyllabus(activity, action, table, degree, course, semester)
    }

    fun getSyllabusData(): LiveData<CommonResponseModel<SyllabusModel>>? {
        return syllabusDataObservable
    }
}