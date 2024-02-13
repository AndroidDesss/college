package com.desss.collegeproduct.module.commonDashBoardFragment.notification.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.module.commonDashBoardFragment.notification.model.NotificationModel
import com.desss.collegeproduct.module.commonDashBoardFragment.notification.repository.NotificationRepository

@SuppressLint("StaticFieldLeak")
class NotificationFragmentScreenViewModel(application: Application, val activity: Activity) :
    AndroidViewModel(application) {
    private var notificationDataObservable: LiveData<CommonResponseModel<NotificationModel>>? = null

    fun callNotificationApi(activity: Activity, action: String, table: String) {
        notificationDataObservable = NotificationRepository.notification(activity, action, table)
    }

    fun getNotificationData(): LiveData<CommonResponseModel<NotificationModel>>? {
        return notificationDataObservable
    }

}