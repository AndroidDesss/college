package com.desss.collegeproduct.module.professorSubModule.notes.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.professorSubModule.notes.model.UploadSuccessModel
import com.desss.collegeproduct.module.professorSubModule.notes.repository.ProfessorNotesRepository
import com.desss.collegeproduct.module.professorSubModule.report.model.ProfessorStudentReportModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


@SuppressLint("StaticFieldLeak")
class ProfessorNotesFragmentScreenViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {
    private var dropDownValuesDataObservable: LiveData<CommonResponseModel<ProfessorStudentReportModel>>? =
        null

    private var uploadPdfValuesDataObservable: LiveData<CommonResponseModel<UploadSuccessModel>>? =
        null

    fun callDropDownValuesApi(activity: Activity, action: String, table: String, userId: String) {
        dropDownValuesDataObservable =
            ProfessorNotesRepository.getDropDownValues(activity, action, table, userId)
    }

    fun getDropDownValuesData(): LiveData<CommonResponseModel<ProfessorStudentReportModel>>? {
        return dropDownValuesDataObservable
    }

    fun callPdfUploadValuesApi(
        activity: Activity,
        action: String,
        professorId: String,
        degree: String,
        department: String,
        section: String,
        semester: String,
        name: String,
        fileSelectedPath: String
    ) {
        val pdfFile = File(fileSelectedPath)
        val requestBody =
            RequestBody.create("application/pdf".toMediaTypeOrNull(), fileSelectedPath)
        val pdfFilePart =
            MultipartBody.Part.createFormData("pdf", pdfFile.name + ".pdf", requestBody)
        uploadPdfValuesDataObservable = ProfessorNotesRepository.uploadPdfFileWithMultiPart(
            activity,
            action,
            professorId,
            degree,
            department,
            section,
            semester,
            name,
            pdfFilePart
        )
    }

    fun getPdfUploadValuesData(): LiveData<CommonResponseModel<UploadSuccessModel>>? {
        return uploadPdfValuesDataObservable
    }

}