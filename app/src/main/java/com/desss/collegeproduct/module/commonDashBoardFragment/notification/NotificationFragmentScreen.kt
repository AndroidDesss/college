package com.desss.collegeproduct.module.commonDashBoardFragment.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.FragmentNotificationScreenBinding


class NotificationFragmentScreen : Fragment() {

    private lateinit var fragmentNotificationScreenBinding: FragmentNotificationScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        fragmentNotificationScreenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification_screen, container, false)

        return fragmentNotificationScreenBinding.root
    }

}