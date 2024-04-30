package com.desss.collegeproduct.module.studentSubModule.transport.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.FragmentTransportBinding
import com.desss.collegeproduct.module.studentSubModule.transport.model.TransportModel
import com.desss.collegeproduct.module.studentSubModule.transport.viewModel.TransportViewModel


class TransportFragment : Fragment() {

    private lateinit var fragmentTransportFragmentBinding: FragmentTransportBinding

    private lateinit var transportViewModel: TransportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentTransportFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_transport, container, false)
        initViewModel()
        callApi()
        observeViewModel(transportViewModel)
        return fragmentTransportFragmentBinding.root
    }

    private fun initViewModel() {
        transportViewModel =
            TransportViewModel(activity?.application!!, requireActivity())
    }
    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        transportViewModel.callTransportApi(
            requireActivity(), "get_tms", SharedPref.getBusNo(context).toString())
    }

    private fun observeViewModel(viewModel: TransportViewModel) {
        viewModel.getLmsLessonData()?.observe(requireActivity(), Observer { tmsData ->
            if (tmsData != null) {
                if (tmsData.status == 403 && tmsData.data.isNotEmpty()) {
                    CommonUtility.cancelProgressDialog(activity)
                } else {
                    handleTmsLessonData(tmsData)
                }
            } else {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
    }

    private fun handleTmsLessonData(response: CommonResponseModel<TransportModel>) {
        if (response.status == 200) {
            CommonUtility.cancelProgressDialog(activity)
            val tmsDataList: List<TransportModel> = response.data
            val userProfile: TransportModel? = tmsDataList.firstOrNull()
            userProfile?.let {
                fragmentTransportFragmentBinding.viaValueTv.text = it.via
                fragmentTransportFragmentBinding.routeValueTv.text = it.route
                fragmentTransportFragmentBinding.contactNumberValueTv.text = it.phone_no
                fragmentTransportFragmentBinding.driverNameValueTv.text = it.name
            }
        }
    }


}