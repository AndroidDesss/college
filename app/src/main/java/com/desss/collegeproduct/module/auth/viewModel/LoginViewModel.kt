package com.desss.collegeproduct.module.auth.viewModel


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.auth.model.LoginModel
import com.desss.collegeproduct.module.auth.repository.LoginRepository
import com.desss.collegeproduct.module.professorSubModule.notes.repository.ProfessorNotesRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@SuppressLint("StaticFieldLeak")
class LoginViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {
    private var loginSubmitObservable: LiveData<CommonResponseModel<LoginModel>>? = null

    fun submitLogin(
        activity: Activity,
        action: String,
        email: String,
        password: String
    ) {
        loginSubmitObservable = LoginRepository.submitLogin(activity, action, email, password)
    }

    fun submitLoginObservable(): LiveData<CommonResponseModel<LoginModel>>? {
        return loginSubmitObservable
    }
}