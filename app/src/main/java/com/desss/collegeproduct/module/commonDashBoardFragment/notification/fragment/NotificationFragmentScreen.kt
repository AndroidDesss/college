package com.desss.collegeproduct.module.commonDashBoardFragment.notification.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.databinding.FragmentNotificationScreenBinding
import com.desss.collegeproduct.module.commonDashBoardFragment.notification.adapter.NotificationAdapter
import com.desss.collegeproduct.module.commonDashBoardFragment.notification.model.NotificationModel
import com.desss.collegeproduct.module.commonDashBoardFragment.notification.viewModel.NotificationFragmentScreenViewModel


class NotificationFragmentScreen : Fragment() {

    private lateinit var fragmentNotificationScreenBinding: FragmentNotificationScreenBinding

    private lateinit var fragmentNotificationFragmentScreenViewModel: NotificationFragmentScreenViewModel

    private var notificationList = arrayListOf<NotificationModel>()

    private var notificationAdapter: NotificationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentNotificationScreenBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_notification_screen,
            container,
            false
        )
        initViewModel()
        callApi()
        observeViewModel(fragmentNotificationFragmentScreenViewModel)
        return fragmentNotificationScreenBinding.root
    }

    private fun initViewModel() {
        fragmentNotificationFragmentScreenViewModel =
            NotificationFragmentScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        fragmentNotificationFragmentScreenViewModel.callNotificationApi(
            requireActivity(),
            "read",
            "general_notifications"
        )
    }

    private fun observeViewModel(viewModel: NotificationFragmentScreenViewModel) {

        viewModel.getNotificationData()?.observe(requireActivity(), Observer { notificationData ->
            if (notificationData != null) {
                if (notificationData.status == 403 && notificationData.data.isNotEmpty()) {
                    CommonUtility.cancelProgressDialog(activity)
                    fragmentNotificationScreenBinding.rlError.visibility = View.VISIBLE
                } else {
                    fragmentNotificationScreenBinding.rlError.visibility = View.GONE
                    handleNotificationData(notificationData)
                }
            } else {
                CommonUtility.cancelProgressDialog(activity)
                fragmentNotificationScreenBinding.rlError.visibility = View.VISIBLE
            }
        })
    }

    private fun handleNotificationData(notificationData: CommonResponseModel<NotificationModel>?) {
        notificationList.clear()
        notificationData?.let {
            val filteredItems = it.data.filter { notification ->
                notification.status == "1" && notification.is_deleted == "0"
            }
            notificationList.addAll(filteredItems)
            setBindingAdapter(notificationList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setBindingAdapter(notificationModel: List<NotificationModel>) {
        notificationAdapter = NotificationAdapter(context, notificationModel)
        fragmentNotificationScreenBinding.recyclerView.adapter = notificationAdapter
        notificationAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(context)
    }

}





