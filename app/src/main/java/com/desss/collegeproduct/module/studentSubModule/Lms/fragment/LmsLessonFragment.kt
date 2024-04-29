package com.desss.collegeproduct.module.studentSubModule.Lms.fragment

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
import com.desss.collegeproduct.databinding.FragmentLmsLessonBinding
import com.desss.collegeproduct.module.studentSubModule.Lms.adapter.LmsLessonAdapter
import com.desss.collegeproduct.module.studentSubModule.Lms.model.LmsModel
import com.desss.collegeproduct.module.studentSubModule.Lms.viewModel.LmsLessonScreenViewModel

class LmsLessonFragment : Fragment() {

    private lateinit var fragmentLmsLessonBinding: FragmentLmsLessonBinding

    private lateinit var lmsLessonScreenViewModel: LmsLessonScreenViewModel

    private var lmsLessonAdapter: LmsLessonAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentLmsLessonBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_lms_lesson, container, false)
        initViewModel()
        callApi()
        observeViewModel(lmsLessonScreenViewModel)
        return fragmentLmsLessonBinding.root
    }

    private fun initViewModel() {
        lmsLessonScreenViewModel =
            LmsLessonScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        lmsLessonScreenViewModel.callLmsLessonApi(
            requireActivity(), "lms", SharedPref.getDegree(context).toString(),
            SharedPref.getCourse(context).toString(),SharedPref.getSemester(context).toString()
        )
    }

    private fun observeViewModel(viewModel: LmsLessonScreenViewModel) {
        viewModel.getLmsLessonData()?.observe(requireActivity(), Observer { lmsData ->
            if (lmsData != null) {
                if (lmsData.status == 403 && lmsData.data.isNotEmpty()) {
                    CommonUtility.cancelProgressDialog(activity)
                    fragmentLmsLessonBinding.rlError.visibility = View.VISIBLE
                } else {
                    fragmentLmsLessonBinding.rlError.visibility = View.GONE
                    handleLmsLessonData(lmsData)
                }
            } else {
                CommonUtility.cancelProgressDialog(activity)
                fragmentLmsLessonBinding.rlError.visibility = View.VISIBLE
            }
        })
    }

    private fun handleLmsLessonData(response: CommonResponseModel<LmsModel>) {
        if (response.status == 200) {
            val lmsList: List<LmsModel> = response.data
            setBindingAdapter(lmsList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setBindingAdapter(lmsModel: List<LmsModel>) {
        lmsLessonAdapter = LmsLessonAdapter(context, lmsModel)
        fragmentLmsLessonBinding.recyclerView.adapter = lmsLessonAdapter
        lmsLessonAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(context)
    }
}