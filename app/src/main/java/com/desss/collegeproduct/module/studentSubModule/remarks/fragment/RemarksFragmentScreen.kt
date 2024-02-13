package com.desss.collegeproduct.module.studentSubModule.remarks.fragment

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
import com.desss.collegeproduct.databinding.FragmentRemarksScreenBinding
import com.desss.collegeproduct.module.studentSubModule.remarks.adapter.RemarksAdapter
import com.desss.collegeproduct.module.studentSubModule.remarks.model.RemarksModel
import com.desss.collegeproduct.module.studentSubModule.remarks.viewModel.RemarksFragmentScreenViewModel

class RemarksFragmentScreen : Fragment() {

    private lateinit var fragmentRemarksScreenBinding: FragmentRemarksScreenBinding

    private lateinit var remarksFragmentScreenViewModel: RemarksFragmentScreenViewModel

    private var remarksList = arrayListOf<RemarksModel>()

    private var remarksAdapter: RemarksAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fragmentRemarksScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_remarks_screen, container, false)
        initViewModel()
        callApi()
        observeViewModel(remarksFragmentScreenViewModel)
        return fragmentRemarksScreenBinding.root
    }

    private fun initViewModel() {
        remarksFragmentScreenViewModel =
            RemarksFragmentScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        remarksFragmentScreenViewModel.callRemarksApi(
            requireActivity(),
            "read",
            "student_remarks",
            SharedPref.getId(context).toString()
        )
    }

    private fun observeViewModel(viewModel: RemarksFragmentScreenViewModel) {

        viewModel.getRemarksData()?.observe(requireActivity(), Observer { remarksData ->
            if (remarksData != null) {
                if (remarksData.status == 403 && remarksData.data.isNotEmpty()) {
                    CommonUtility.cancelProgressDialog(activity)
                    fragmentRemarksScreenBinding.rlError.visibility = View.VISIBLE
                } else {
                    fragmentRemarksScreenBinding.rlError.visibility = View.GONE
                    handleRemarksData(remarksData)
                }
            } else {
                CommonUtility.cancelProgressDialog(activity)
                fragmentRemarksScreenBinding.rlError.visibility = View.VISIBLE
            }
        })
    }

    private fun handleRemarksData(syllabusData: CommonResponseModel<RemarksModel>?) {
        remarksList.clear()
        syllabusData?.let {
            val filteredItems = it.data.filter { remarks ->
                remarks.status == "1" && remarks.is_deleted == "0"
            }
            remarksList.addAll(filteredItems)
            setBindingAdapter(remarksList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setBindingAdapter(remarksModel: List<RemarksModel>) {
        remarksAdapter = RemarksAdapter(context, remarksModel)
        fragmentRemarksScreenBinding.recyclerView.adapter = remarksAdapter
        remarksAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(context)
    }

}