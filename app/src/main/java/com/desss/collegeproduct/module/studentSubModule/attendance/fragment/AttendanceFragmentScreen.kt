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
            val attendanceDataList = response.data
            attendanceDataList.forEach { attendance ->
                callMonthlyHolidayApi(attendance.Month,attendance.Year,attendance,attendanceDataList)
            }
        }
    }



    @SuppressLint("NotifyDataSetChanged")
    private fun setBindingAdapter(studentAttendanceModel: List<StudentAttendanceModel>) {
        studentAttendanceAdapter = StudentAttendanceAdapter(context, studentAttendanceModel)
        fragmentAttendanceScreenBinding.recyclerView.adapter = studentAttendanceAdapter
        studentAttendanceAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(context)
    }

    private fun callMonthlyHolidayApi(
        month: String,
        year: String,
        attendanceModel: StudentAttendanceModel,
        attendanceDataList: List<StudentAttendanceModel>
    ) {
        // Make the API call
        attendanceFragmentScreenViewModel.callMonthlyHolidaysApi(
            requireActivity(),
            "read",
            "master_monthly",
            month,
            year
        )

        // Observe the LiveData for the response
        attendanceFragmentScreenViewModel.getMonthlyHolidaysData()?.observeForever { holidaysData ->
            if (holidaysData.status == 200 && holidaysData.data.isNotEmpty()) {
                val holidaysString = holidaysData.data[0].holidays
                val holidayCount = holidaysString.split(",").size
                attendanceModel.holidayCount = holidayCount
                setUpdatedAttendanceData(attendanceModel, attendanceDataList)
            } else {
                attendanceModel.holidayCount = 0
                setUpdatedAttendanceData(attendanceModel, attendanceDataList)
            }
            CommonUtility.cancelProgressDialog(context)
        }
    }

    private fun setUpdatedAttendanceData(attendanceModel: StudentAttendanceModel, attendanceDataList: List<StudentAttendanceModel>) {
        val updatedAttendanceDataList = mutableListOf<StudentAttendanceModel>()
        updatedAttendanceDataList.add(attendanceModel)
        if (updatedAttendanceDataList.size == attendanceDataList.size) {
            setBindingAdapter(updatedAttendanceDataList)
        }
    }

}