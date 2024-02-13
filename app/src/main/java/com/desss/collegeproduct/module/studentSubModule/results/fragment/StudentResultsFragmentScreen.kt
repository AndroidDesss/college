package com.desss.collegeproduct.module.studentSubModule.results.fragment

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
import com.desss.collegeproduct.databinding.FragmentStudentResultsScreenBinding
import com.desss.collegeproduct.module.studentSubModule.examTimeTable.model.ExamTimeTableModel
import com.desss.collegeproduct.module.studentSubModule.examTimeTable.viewModel.ExamTimeTableFragmentScreenViewModel
import com.desss.collegeproduct.module.studentSubModule.results.adapter.StudentResultsAdapter
import com.desss.collegeproduct.module.studentSubModule.results.viewModel.ExamResultsFragmentScreenViewModel


class StudentResultsFragmentScreen : Fragment() {

    private lateinit var fragmentStudentResultsScreenBinding: FragmentStudentResultsScreenBinding

    private lateinit var examTimeTableFragmentScreenViewModel: ExamTimeTableFragmentScreenViewModel

    private lateinit var examResultsFragmentScreenViewModel: ExamResultsFragmentScreenViewModel

    private var examTimeTableList = arrayListOf<ExamTimeTableModel>()

    private var studentResultsAdapter: StudentResultsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentStudentResultsScreenBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_student_results_screen,
            container,
            false
        )
        initViewModel()
        callApi()
        observeViewModel(examTimeTableFragmentScreenViewModel)
        return fragmentStudentResultsScreenBinding.root
    }

    private fun initViewModel() {
        examTimeTableFragmentScreenViewModel =
            ExamTimeTableFragmentScreenViewModel(activity?.application!!, requireActivity())

        examResultsFragmentScreenViewModel =
            ExamResultsFragmentScreenViewModel(activity?.application!!, requireActivity())
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
                    fragmentStudentResultsScreenBinding.rlError.visibility = View.VISIBLE
                } else {
                    fragmentStudentResultsScreenBinding.rlError.visibility = View.GONE
                    handleExamTimeTableData(examTimeTable)
                }
            } else {
                CommonUtility.cancelProgressDialog(activity)
                fragmentStudentResultsScreenBinding.rlError.visibility = View.VISIBLE
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
        studentResultsAdapter = StudentResultsAdapter(context, examTimeTableModel,examResultsFragmentScreenViewModel)
        fragmentStudentResultsScreenBinding.recyclerView.adapter = studentResultsAdapter
        studentResultsAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(context)
    }

}