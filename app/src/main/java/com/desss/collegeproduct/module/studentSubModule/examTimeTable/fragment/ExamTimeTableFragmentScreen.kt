package com.desss.collegeproduct.module.studentSubModule.examTimeTable.fragment

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
import com.desss.collegeproduct.databinding.FragmentExamTimeTableScreenBinding
import com.desss.collegeproduct.module.studentSubModule.examTimeTable.adapter.ExamTimeTableAdapter
import com.desss.collegeproduct.module.studentSubModule.examTimeTable.model.ExamTimeTableModel
import com.desss.collegeproduct.module.studentSubModule.examTimeTable.viewModel.ExamTimeTableFragmentScreenViewModel


class ExamTimeTableFragmentScreen : Fragment() {

    private lateinit var fragmentExamTimeTableScreenBinding: FragmentExamTimeTableScreenBinding

    private lateinit var examTimeTableFragmentScreenViewModel: ExamTimeTableFragmentScreenViewModel

    private var examTimeTableList = arrayListOf<ExamTimeTableModel>()

    private var examTimeTableAdapter: ExamTimeTableAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentExamTimeTableScreenBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_exam_time_table_screen,
            container,
            false
        )
        initViewModel()
        callApi()
        observeViewModel(examTimeTableFragmentScreenViewModel)
        return fragmentExamTimeTableScreenBinding.root
    }

    private fun initViewModel() {
        examTimeTableFragmentScreenViewModel =
            ExamTimeTableFragmentScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        examTimeTableFragmentScreenViewModel.callExamTimeTableApi(
            requireActivity(),
            "read",
            "student_timetables",
            SharedPref.getDegree(context).toString(),
            SharedPref.getCourse(context).toString(),
            SharedPref.getSemester(context).toString()
        )
    }

    private fun observeViewModel(viewModel: ExamTimeTableFragmentScreenViewModel) {

        viewModel.getExamTimeTableData()?.observe(requireActivity(), Observer { examTimeTable ->
            if (examTimeTable != null) {
                if (examTimeTable.status == 403 && examTimeTable.data.isNotEmpty()) {
                    CommonUtility.cancelProgressDialog(activity)
                    fragmentExamTimeTableScreenBinding.rlError.visibility = View.VISIBLE
                } else {
                    fragmentExamTimeTableScreenBinding.rlError.visibility = View.GONE
                    handleExamTimeTableData(examTimeTable)
                }
            } else {
                CommonUtility.cancelProgressDialog(activity)
                fragmentExamTimeTableScreenBinding.rlError.visibility = View.VISIBLE
            }
        })
    }

    private fun handleExamTimeTableData(syllabusData: CommonResponseModel<ExamTimeTableModel>?) {
        examTimeTableList.clear()
        syllabusData?.let {
            val filteredItems = it.data.filter { syllabus ->
                syllabus.status == "1" && syllabus.is_deleted == "0"
            }
            examTimeTableList.addAll(filteredItems)
            setBindingAdapter(examTimeTableList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setBindingAdapter(examTimeTableModel: List<ExamTimeTableModel>) {
        examTimeTableAdapter = ExamTimeTableAdapter(context, examTimeTableModel)
        fragmentExamTimeTableScreenBinding.recyclerView.adapter = examTimeTableAdapter
        examTimeTableAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(context)
    }

}