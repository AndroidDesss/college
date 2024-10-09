package com.desss.collegeproduct.module.studentSubModule.attendance.fragment

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
import com.desss.collegeproduct.databinding.FragmentAttendanceScreenBinding
import com.desss.collegeproduct.module.studentSubModule.attendance.adapter.StudentAttendanceAdapter
import com.desss.collegeproduct.module.studentSubModule.attendance.model.StudentAttendanceModel
import com.desss.collegeproduct.module.studentSubModule.attendance.viewModel.AttendanceFragmentScreenViewModel
class AttendanceFragmentScreen : Fragment() {

    private lateinit var fragmentAttendanceScreenBinding: FragmentAttendanceScreenBinding

    private lateinit var attendanceFragmentScreenViewModel: AttendanceFragmentScreenViewModel

    private var studentAttendanceAdapter: StudentAttendanceAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentAttendanceScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_attendance_screen, container, false)
        initViewModel()
        callApi()
        observeViewModel(attendanceFragmentScreenViewModel)
        return fragmentAttendanceScreenBinding.root
    }

    private fun initViewModel() {
        attendanceFragmentScreenViewModel =
            AttendanceFragmentScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        attendanceFragmentScreenViewModel.callAttendanceApi(
            requireActivity(),
            "student_attendence",
            SharedPref.getId(context).toString()
        )
    }

    private fun observeViewModel(viewModel: AttendanceFragmentScreenViewModel) {
        viewModel.getAttendanceData()?.observe(requireActivity(),
            Observer { attendanceData ->
                if (attendanceData != null) {
                    if (attendanceData.status == 403 && attendanceData.data.isNotEmpty()) {
                        CommonUtility.cancelProgressDialog(activity)
                        fragmentAttendanceScreenBinding.rlError.visibility = View.VISIBLE
                    } else {
                        fragmentAttendanceScreenBinding.rlError.visibility = View.GONE
                        handleAttendanceData(attendanceData)
                    }
                } else {
                    CommonUtility.cancelProgressDialog(activity)
                    fragmentAttendanceScreenBinding.rlError.visibility = View.VISIBLE
                }
            })
    }

    private fun handleAttendanceData(response: CommonResponseModel<StudentAttendanceModel>) {
        if (response.status == 200) {
            CommonUtility.cancelProgressDialog(context)
            val attendanceDataList: List<StudentAttendanceModel> = response.data
            setBindingAdapter(attendanceDataList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setBindingAdapter(studentAttendanceModel: List<StudentAttendanceModel>) {
        studentAttendanceAdapter = StudentAttendanceAdapter(context, studentAttendanceModel)
        fragmentAttendanceScreenBinding.recyclerView.adapter = studentAttendanceAdapter
        studentAttendanceAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(context)
    }

}