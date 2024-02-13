package com.desss.collegeproduct.module.commonDashBoardFragment.profile.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.commonDashBoardFragment.profile.model.ProfileModel
import com.desss.collegeproduct.module.commonDashBoardFragment.profile.repository.ProfileRepository

@SuppressLint("StaticFieldLeak")
class ProfileFragmentScreenViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {

    private var studentProfileDataObservable: LiveData<CommonResponseModel<ProfileModel>>? = null

    fun callStudentProfileApi(activity: Activity, action: String, table: String, id: String) {
        studentProfileDataObservable =
            ProfileRepository.studentProfileData(activity, action, table, id)
    }

    fun getStudentProfileData(): LiveData<CommonResponseModel<ProfileModel>>? {
        return studentProfileDataObservable
    }
}