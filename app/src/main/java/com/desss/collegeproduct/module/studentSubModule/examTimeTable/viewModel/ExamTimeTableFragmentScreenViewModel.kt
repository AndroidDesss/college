package com.desss.collegeproduct.module.studentSubModule.examTimeTable.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.studentSubModule.examTimeTable.model.ExamTimeTableModel
import com.desss.collegeproduct.module.studentSubModule.examTimeTable.repository.ExamTimeTableRepository

@SuppressLint("StaticFieldLeak")
class ExamTimeTableFragmentScreenViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {
    private var examTimeTableDataObservable: LiveData<CommonResponseModel<ExamTimeTableModel>>? =
        null

    fun callExamTimeTableApi(
        activity: Activity,
        action: String,
        table: String,
        degree: String,
        course: String,
        semester: String
    ) {
        examTimeTableDataObservable = ExamTimeTableRepository.getExamTimeTable(
            activity,
            action,
            table,
            degree,
            course,
            semester
        )
    }

    fun getExamTimeTableData(): LiveData<CommonResponseModel<ExamTimeTableModel>>? {
        return examTimeTableDataObservable
    }
}
