package com.desss.collegeproduct.module.studentSubModule.feePay.fragment

import android.annotation.SuppressLint
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
import com.desss.collegeproduct.databinding.FragmentFeePayScreenBinding
import com.desss.collegeproduct.module.studentSubModule.feePay.adapter.FeePayAdapter
import com.desss.collegeproduct.module.studentSubModule.feePay.model.FeePayModel
import com.desss.collegeproduct.module.studentSubModule.feePay.viewModel.FeePayFragmentScreenViewModel

class FeePayFragmentScreen : Fragment() {

    private lateinit var fragmentFeePayScreenBinding: FragmentFeePayScreenBinding

    private lateinit var feePayFragmentScreenViewModel: FeePayFragmentScreenViewModel

    private var feePayAdapter: FeePayAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentFeePayScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_fee_pay_screen, container, false)
        initViewModel()
        callApi()
        observeViewModel(feePayFragmentScreenViewModel)
        return fragmentFeePayScreenBinding.root
    }

    private fun initViewModel() {
        feePayFragmentScreenViewModel =
            FeePayFragmentScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        feePayFragmentScreenViewModel.callFeePayApi(
            requireActivity(), "read", "student_fees",
            SharedPref.getId(context).toString()
        )
    }

    private fun observeViewModel(viewModel: FeePayFragmentScreenViewModel) {

        viewModel.getFeePayData()?.observe(requireActivity(), Observer { feePayData ->
            if (feePayData != null) {
                if (feePayData.status == 403 && feePayData.data.isNotEmpty()) {
                    CommonUtility.cancelProgressDialog(activity)
                    fragmentFeePayScreenBinding.rlError.visibility = View.VISIBLE
                } else {
                    fragmentFeePayScreenBinding.rlError.visibility = View.GONE
                    handleFeePayData(feePayData)
                }
            } else {
                CommonUtility.cancelProgressDialog(activity)
                fragmentFeePayScreenBinding.rlError.visibility = View.VISIBLE
            }
        })
    }

    private fun handleFeePayData(response: CommonResponseModel<FeePayModel>) {

        if (response.status == 200) {
            val feePayList: List<FeePayModel> = response.data
            setBindingAdapter(feePayList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setBindingAdapter(feePayModel: List<FeePayModel>) {

        feePayAdapter = FeePayAdapter(context, feePayModel)
        fragmentFeePayScreenBinding.recyclerView.adapter = feePayAdapter
        feePayAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(context)
    }

}