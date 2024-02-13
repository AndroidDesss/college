package com.desss.collegeproduct.module.studentSubModule.syllabus.fragment

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
import com.desss.collegeproduct.databinding.FragmentSyllabusScreenBinding
import com.desss.collegeproduct.module.studentSubModule.syllabus.adapter.SyllabusAdapter
import com.desss.collegeproduct.module.studentSubModule.syllabus.model.SyllabusModel
import com.desss.collegeproduct.module.studentSubModule.syllabus.viewModel.SyllabusFragmentScreenViewModel


class SyllabusFragmentScreen : Fragment() {

    private lateinit var fragmentSyllabusScreenBinding: FragmentSyllabusScreenBinding

    private lateinit var syllabusFragmentScreenViewModel: SyllabusFragmentScreenViewModel

    private var syllabusList = arrayListOf<SyllabusModel>()

    private var syllabusAdapter: SyllabusAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSyllabusScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_syllabus_screen, container, false)
        initViewModel()
        callApi()
        observeViewModel(syllabusFragmentScreenViewModel)
        return fragmentSyllabusScreenBinding.root
    }

    private fun initViewModel() {
        syllabusFragmentScreenViewModel =
            SyllabusFragmentScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        syllabusFragmentScreenViewModel.callSyllabusApi(
            requireActivity(),
            "read",
            "student_sylabus",
            SharedPref.getDegree(context).toString(),
            SharedPref.getCourse(context).toString(),
            SharedPref.getSemester(context).toString()
        )
    }

    private fun observeViewModel(viewModel: SyllabusFragmentScreenViewModel) {
        viewModel.getSyllabusData()?.observe(requireActivity(), Observer { syllabusResponse ->
            if (syllabusResponse != null) {
                if (syllabusResponse.status == 403 && syllabusResponse.data.isNotEmpty()) {
                    CommonUtility.cancelProgressDialog(activity)
                    fragmentSyllabusScreenBinding.rlError.visibility = View.VISIBLE
                } else {
                    fragmentSyllabusScreenBinding.rlError.visibility = View.GONE
                    handleSyllabusData(syllabusResponse)
                }
            } else {
                CommonUtility.cancelProgressDialog(activity)
                fragmentSyllabusScreenBinding.rlError.visibility = View.VISIBLE
            }
        })
    }

    private fun handleSyllabusData(syllabusData: CommonResponseModel<SyllabusModel>?) {
        syllabusList.clear()
        syllabusData?.let {
            val filteredItems = it.data.filter { syllabus ->
                syllabus.status == "1" && syllabus.is_deleted == "0"
            }
            syllabusList.addAll(filteredItems)
            setBindingAdapter(syllabusList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setBindingAdapter(syllabusModel: List<SyllabusModel>) {
        syllabusAdapter = SyllabusAdapter(context, syllabusModel)
        fragmentSyllabusScreenBinding.recyclerView.adapter = syllabusAdapter
        syllabusAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(context)
    }
}