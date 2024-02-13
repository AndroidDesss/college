package com.desss.collegeproduct.module.professorSubModule.professorAttendance.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.FragmentProfessorAttendanceScreenBinding
import com.desss.collegeproduct.databinding.ShowStudentsListPopupBinding
import com.desss.collegeproduct.module.professorSubModule.professorAttendance.adapter.ProfessorNameListAdapter
import com.desss.collegeproduct.module.professorSubModule.professorAttendance.model.CheckProfessorAttendanceModel
import com.desss.collegeproduct.module.professorSubModule.professorAttendance.model.ProfessorCountModel
import com.desss.collegeproduct.module.professorSubModule.professorAttendance.viewmodel.ProfessorAttendanceFragmentScreenViewModel
import java.util.Calendar
import java.util.Locale

class ProfessorAttendanceFragmentScreen : Fragment() {

    private lateinit var fragmentProfessorAttendanceScreenBinding: FragmentProfessorAttendanceScreenBinding

    private lateinit var professorAttendanceFragmentScreenViewModel: ProfessorAttendanceFragmentScreenViewModel

    private var totalProfessorPresentList = arrayListOf<String>()

    private var professorNameListAdapter: ProfessorNameListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProfessorAttendanceScreenBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_professor_attendance_screen,
            container,
            false
        )
        initViewModel()
        callApi()
        initListener()
        getMonthDropDownValues()
        observeViewModel(professorAttendanceFragmentScreenViewModel, 1)
        return fragmentProfessorAttendanceScreenBinding.root
    }

    private fun initListener() {
        fragmentProfessorAttendanceScreenBinding.btnPresent.setOnClickListener(onClickListener)
        fragmentProfessorAttendanceScreenBinding.presentLinear.setOnClickListener(onClickListener)
        fragmentProfessorAttendanceScreenBinding.monthSpinner.onItemSelectedListener =
            onItemSelectedListener
    }

    private fun markAttendanceForProfessor() {
        CommonUtility.showProgressDialog(context)
        professorAttendanceFragmentScreenViewModel.callMarkProfessorAttendanceApi(
            requireActivity(),
            "professor_mark_attendence_myself",
            SharedPref.getId(context).toString()
        )
    }


    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btnPresent -> {
                markAttendanceForProfessor()
                observeViewModel(professorAttendanceFragmentScreenViewModel, 3)
            }

            R.id.presentLinear -> {
                if (fragmentProfessorAttendanceScreenBinding.presentValueTv.text.toString() != "0") {
                    showPopupProfessorList(view, "Present List", totalProfessorPresentList)
                }
            }
        }
    }

    private val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (parent?.id) {
                R.id.monthSpinner -> {
                    if (parent.getItemAtPosition(position).toString() != "Select Month") {
                        val month = parent.getItemAtPosition(position).toString()
                        val subMonthYear = month.split(" ")
                        callProfessorCountApi(subMonthYear[0], subMonthYear[1])
                        observeViewModel(professorAttendanceFragmentScreenViewModel, 2)
                    } else {
                        fragmentProfessorAttendanceScreenBinding.presentValueTv.text = "0"
                        fragmentProfessorAttendanceScreenBinding.absentValueTv.text = "0"
                        fragmentProfessorAttendanceScreenBinding.totalValueTv.text = "0"
                        fragmentProfessorAttendanceScreenBinding.lateValueTv.text = "0"
                    }
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        fragmentProfessorAttendanceScreenBinding.professorNameTv.text =
            CommonUtility.getCurrentDate(context)
        professorAttendanceFragmentScreenViewModel.callCheckProfessorAttendanceApi(
            requireActivity(),
            "check_today_marked_or_leave",
            SharedPref.getId(context).toString()
        )
    }

    private fun callProfessorCountApi(month: String, year: String) {
        CommonUtility.showProgressDialog(context)
        fragmentProfessorAttendanceScreenBinding.professorNameTv.text =
            CommonUtility.getCurrentDate(context)
        professorAttendanceFragmentScreenViewModel.callProfessorCountApi(
            requireActivity(),
            "professor_counts",
            SharedPref.getId(context).toString(),
            month,
            year
        )
    }

    private fun initViewModel() {
        professorAttendanceFragmentScreenViewModel =
            ProfessorAttendanceFragmentScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun getMonthDropDownValues() {
        val calendar = Calendar.getInstance()
        val dateFormat = java.text.SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val pastMonthsList = mutableListOf<String>()
        calendar.add(Calendar.MONTH, -1)
        repeat(6) {
            pastMonthsList.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.MONTH, -1)
        }
        val updatedMonthsList = mutableListOf("Select Month").apply {
            addAll(pastMonthsList)
        }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, updatedMonthsList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fragmentProfessorAttendanceScreenBinding.monthSpinner.adapter = adapter
    }

    private fun observeViewModel(
        viewModel: ProfessorAttendanceFragmentScreenViewModel,
        position: Int
    ) {
        if (position == 1) {
            viewModel.getCheckProfessorAttendanceData()
                ?.observe(requireActivity(), Observer { professorAttendanceData ->
                    if (professorAttendanceData != null) {
                        if (professorAttendanceData.status == 403 && professorAttendanceData.data.isNotEmpty()) {
                            CommonUtility.cancelProgressDialog(activity)
                        } else {
                            handleProfessorAttendanceData(professorAttendanceData)
                            CommonUtility.cancelProgressDialog(activity)
                        }
                    } else {
                        CommonUtility.cancelProgressDialog(activity)
                    }
                })
        } else if (position == 3) {
            viewModel.getMarkProfessorAttendanceData()
                ?.observe(requireActivity(), Observer { markProfessorAttendance ->
                    if (markProfessorAttendance != null) {
                        if (markProfessorAttendance.status == 401 && markProfessorAttendance.data.isNotEmpty()) {
                            CommonUtility.cancelProgressDialog(activity)
                        } else {
                            CommonUtility.cancelProgressDialog(activity)
                            CommonUtility.toastString("Attendance Marked Successfully..!", activity)
                            fragmentProfessorAttendanceScreenBinding.btnPresent.visibility =
                                View.GONE

                        }
                    } else {
                        CommonUtility.cancelProgressDialog(activity)
                    }
                })
        } else {
            viewModel.getProfessorCountData()
                ?.observe(requireActivity(), Observer { professorAttendanceCountData ->
                    if (professorAttendanceCountData != null) {
                        if (professorAttendanceCountData.status == 401 && professorAttendanceCountData.data.isNotEmpty()) {
                            CommonUtility.cancelProgressDialog(activity)
                            fragmentProfessorAttendanceScreenBinding.presentValueTv.text = "0"
                            fragmentProfessorAttendanceScreenBinding.absentValueTv.text = "0"
                            fragmentProfessorAttendanceScreenBinding.totalValueTv.text = "0"
                            fragmentProfessorAttendanceScreenBinding.lateValueTv.text = "0"
                        } else {
                            handleProfessorAttendanceCountData(professorAttendanceCountData)
                        }
                    } else {
                        CommonUtility.cancelProgressDialog(activity)
                        fragmentProfessorAttendanceScreenBinding.presentValueTv.text = "0"
                        fragmentProfessorAttendanceScreenBinding.absentValueTv.text = "0"
                        fragmentProfessorAttendanceScreenBinding.totalValueTv.text = "0"
                        fragmentProfessorAttendanceScreenBinding.lateValueTv.text = "0"
                    }
                })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleProfessorAttendanceCountData(professorCountsData: CommonResponseModel<ProfessorCountModel>?) {
        CommonUtility.cancelProgressDialog(activity)
        totalProfessorPresentList.clear()
        val professorCountsListTemp: List<ProfessorCountModel> = professorCountsData!!.data
        val userProfile: ProfessorCountModel? = professorCountsListTemp.firstOrNull()
        userProfile?.let {
            fragmentProfessorAttendanceScreenBinding.presentValueTv.text =
                it.total_present_count.toString()
            fragmentProfessorAttendanceScreenBinding.absentValueTv.text =
                it.total_not_present_count.toString()
            fragmentProfessorAttendanceScreenBinding.totalValueTv.text = it.total_count.toString()
            fragmentProfessorAttendanceScreenBinding.lateValueTv.text =
                it.total_late_count.toString()
            totalProfessorPresentList.addAll(it.present_list)

        }
    }

    private fun handleProfessorAttendanceData(professorAttendanceData: CommonResponseModel<CheckProfessorAttendanceModel>?) {
        val professorAttendanceDataList: List<CheckProfessorAttendanceModel> =
            professorAttendanceData!!.data
        val userProfile: CheckProfessorAttendanceModel? = professorAttendanceDataList.firstOrNull()
        userProfile?.let {
            if (it.msg == "You go ahead") {
                fragmentProfessorAttendanceScreenBinding.btnPresent.visibility = View.VISIBLE
            } else {
                fragmentProfessorAttendanceScreenBinding.btnPresent.visibility = View.GONE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showPopupProfessorList(v: View, screenName: String, studentsList: List<String>) {
        val inflater = LayoutInflater.from(context)
        val popupBinding: ShowStudentsListPopupBinding =
            DataBindingUtil.inflate(inflater, R.layout.show_students_list_popup, null, false)
        val popupWindow = PopupWindow(
            popupBinding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        )
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0)
        popupWindow.setFocusable(true)
        popupWindow.update()
        popupBinding.btnClose.setOnClickListener {
            popupWindow.dismiss()
        }
        popupBinding.screenNameTv.text = screenName
        professorNameListAdapter = ProfessorNameListAdapter(context, studentsList)
        popupBinding.recyclerView.adapter = professorNameListAdapter
        professorNameListAdapter!!.notifyDataSetChanged()
    }

}