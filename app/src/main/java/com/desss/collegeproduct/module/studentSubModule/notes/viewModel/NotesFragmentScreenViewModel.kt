package com.desss.collegeproduct.module.studentSubModule.notes.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.studentSubModule.notes.model.NotesModel
import com.desss.collegeproduct.module.studentSubModule.notes.repository.NotesRepository

@SuppressLint("StaticFieldLeak")
class NotesFragmentScreenViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {
    private var notesDataObservable: LiveData<CommonResponseModel<NotesModel>>? = null

    fun callNotesApi(
        activity: Activity,
        action: String,
        degree: String,
        course: String,
        semester: String,
        section: String
    ) {
        notesDataObservable =
            NotesRepository.getNotes(activity, action, degree, course, semester, section)
    }

    fun getNotesData(): LiveData<CommonResponseModel<NotesModel>>? {
        return notesDataObservable
    }
}