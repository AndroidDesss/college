package com.desss.collegeproduct.module.professorSubModule.studentAttendance.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.FragmentStudentAttendanceScreenBinding
import com.desss.collegeproduct.databinding.ShowAttendanceListPopUpBinding
import com.desss.collegeproduct.databinding.ShowStudentsListPopupBinding
import com.desss.collegeproduct.module.professorSubModule.professorAttendance.model.CheckProfessorAttendanceModel
import com.desss.collegeproduct.module.professorSubModule.report.model.ProfessorStudentReportModel
import com.desss.collegeproduct.module.professorSubModule.report.model.StudentListBasedModel
import com.desss.collegeproduct.module.professorSubModule.studentAttendance.adapter.AttendanceStudentsListAdapter
import com.desss.collegeproduct.module.professorSubModule.studentAttendance.adapter.NameListAdapter
import com.desss.collegeproduct.module.professorSubModule.studentAttendance.model.StudentCountModel
import com.desss.collegeproduct.module.professorSubModule.studentAttendance.viewmodel.StudentAttendanceFragmentScreenViewModel
import java.util.Locale

class StudentAttendanceFragmentScreen : Fragment() {

    private lateinit var fragmentStudentAttendanceScreenBinding: FragmentStudentAttendanceScreenBinding

    private lateinit var studentAttendanceFragmentScreenViewModel: StudentAttendanceFragmentScreenViewModel

    private lateinit var selectedDegree: String

    private lateinit var selectedDepartment: String

    private lateinit var selectedSemester: String

    private lateinit var selectedSection: String

    private var studentsList = arrayListOf<StudentListBasedModel>()

    private var totalStudentsPresentList = arrayListOf<String>()

    private var totalStudentsAbsentList = arrayListOf<String>()

    private var attendanceStudentsListAdapter: AttendanceStudentsListAdapter? = null

    private var nameListAdapter: NameListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentStudentAttendanceScreenBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_student_attendance_screen,
            container,
            false
        )
        initViewModel()
        callApi()
        observeViewModel(studentAttendanceFragmentScreenViewModel, 1)
        initListener()
        return fragmentStudentAttendanceScreenBinding.root
    }

    private fun initListener() {
        fragmentStudentAttendanceScreenBinding.btnView.setOnClickListener(onClickListener)
        fragmentStudentAttendanceScreenBinding.presentLinear.setOnClickListener(onClickListener)
        fragmentStudentAttendanceScreenBinding.absentLinear.setOnClickListener(onClickListener)
        fragmentStudentAttendanceScreenBinding.degreeSpinner.onItemSelectedListener =
            onItemSelectedListener
        fragmentStudentAttendanceScreenBinding.departmentSpinner.onItemSelectedListener =
            onItemSelectedListener
        fragmentStudentAttendanceScreenBinding.semesterSpinner.onItemSelectedListener =
            onItemSelectedListener
        fragmentStudentAttendanceScreenBinding.sectionSpinner.onItemSelectedListener =
            onItemSelectedListener
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btnView -> {
                if (selectedDegree == "Select" || selectedDepartment == "Select" || selectedSemester == "Select" || selectedSection == "Select") {
                    CommonUtility.toastString("Please select categories..!", activity)
                } else {
                    showPopup(view)
                }
            }
            R.id.presentLinear -> {
                if (fragmentStudentAttendanceScreenBinding.presentValueTv.text.toString() != "0") {
                    showPopupStudentsList(view, "Present List", totalStudentsPresentList)
                }
            }
            R.id.absentLinear -> {
                if (fragmentStudentAttendanceScreenBinding.absentValueTv.text.toString() != "0") {
                    showPopupStudentsList(view, "Absent List", totalStudentsAbsentList)
                }
            }
        }
    }

    private val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (parent?.id) {
                R.id.degreeSpinner -> {
                    selectedDegree = parent.getItemAtPosition(position).toString()
                    updateDepartmentSpinner(selectedDegree)
                }

                R.id.departmentSpinner -> {
                    selectedDepartment = parent.getItemAtPosition(position).toString()
                    updateSemesterSpinner(selectedDegree, selectedDepartment)
                }

                R.id.semesterSpinner -> {
                    selectedSemester = parent.getItemAtPosition(position).toString()
                    updateSectionSpinner(selectedDegree, selectedDepartment, selectedSemester)
                }

                R.id.sectionSpinner -> {
                    selectedSection = parent.getItemAtPosition(position).toString()
                    when {
                        selectedSection != "Select" -> {
                            checkLeaveApi()
                            observeViewModel(studentAttendanceFragmentScreenViewModel, 3)
                        }
                    }
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    private fun checkLeaveApi() {
        CommonUtility.showProgressDialog(context)
        studentAttendanceFragmentScreenViewModel.callCheckLeaveApi(
            requireActivity(),
            "check_today_marked_or_leave",
            SharedPref.getId(context).toString()
        )
    }

    private fun initViewModel() {
        studentAttendanceFragmentScreenViewModel =
            StudentAttendanceFragmentScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        studentAttendanceFragmentScreenViewModel.callDropDownValuesApi(
            requireActivity(),
            "read",
            "professor_departments",
            SharedPref.getId(context).toString()
        )
    }

    private fun observeViewModel(
        viewModel: StudentAttendanceFragmentScreenViewModel,
        position: Int
    ) {
        if (position == 1) {
            viewModel.getDropDownValuesData()
                ?.observe(requireActivity(), Observer { departmentData ->
                    if (departmentData != null) {
                        if (departmentData.status == 403 && departmentData.data.isNotEmpty()) {
                            CommonUtility.cancelProgressDialog(activity)
                        } else {
                            handleDegreeData(departmentData)
                        }
                    }
                })
        } else if (position == 2) {
            viewModel.getStudentCountListApiValuesData()
                ?.observe(requireActivity(), Observer { studentCountList ->
                    if (studentCountList != null) {
                        if (studentCountList.status == 401 && studentCountList.data.isNotEmpty()) {
                            CommonUtility.cancelProgressDialog(activity)
                            fragmentStudentAttendanceScreenBinding.presentValueTv.text = "0"
                            fragmentStudentAttendanceScreenBinding.absentValueTv.text = "0"
                            fragmentStudentAttendanceScreenBinding.totalValueTv.text = "0"
                        } else {
                            handleStudentsCountListData(studentCountList)
                        }
                    } else {
                        CommonUtility.cancelProgressDialog(activity)
                        fragmentStudentAttendanceScreenBinding.presentValueTv.text = "0"
                        fragmentStudentAttendanceScreenBinding.absentValueTv.text = "0"
                        fragmentStudentAttendanceScreenBinding.totalValueTv.text = "0"
                    }
                })
        }
        else if (position == 3) {
            viewModel.getCheckLeaveData()
                ?.observe(requireActivity(), Observer { leaveData ->
                    if (leaveData != null) {
                        if (leaveData.status == 403 && leaveData.data.isNotEmpty()) {
                            CommonUtility.cancelProgressDialog(activity)
                        } else {
                            fragmentStudentAttendanceScreenBinding.leaveContentTv.visibility = View.GONE
                            handleLeaveData(leaveData)
                        }
                    } else {
                        CommonUtility.cancelProgressDialog(activity)
                        fragmentStudentAttendanceScreenBinding.leaveContentTv.visibility = View.VISIBLE
                    }
                })
        }
    }

    private fun handleLeaveData(leaveData: CommonResponseModel<CheckProfessorAttendanceModel>?) {
        val professorAttendanceDataList: List<CheckProfessorAttendanceModel> =
            leaveData!!.data
        val userProfile: CheckProfessorAttendanceModel? = professorAttendanceDataList.firstOrNull()
        userProfile?.let {
            if (it.msg == "You go ahead") {
                fragmentStudentAttendanceScreenBinding.btnView.visibility = View.VISIBLE
                fragmentStudentAttendanceScreenBinding.attendanceRecapLinearInfo.visibility = View.VISIBLE
                fragmentStudentAttendanceScreenBinding.leaveContentTv.visibility = View.GONE
                callStudentCountListApi()
                observeViewModel(studentAttendanceFragmentScreenViewModel, 2)
            } else {
                CommonUtility.cancelProgressDialog(activity)
                fragmentStudentAttendanceScreenBinding.btnView.visibility = View.GONE
                fragmentStudentAttendanceScreenBinding.attendanceRecapLinearInfo.visibility = View.GONE
                fragmentStudentAttendanceScreenBinding.leaveContentTv.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleStudentsCountListData(studentsCountsData: CommonResponseModel<StudentCountModel>?) {
        CommonUtility.cancelProgressDialog(activity)
        totalStudentsPresentList.clear()
        totalStudentsAbsentList.clear()
        val studentsCountsListTemp: List<StudentCountModel> = studentsCountsData!!.data
        val userProfile: StudentCountModel? = studentsCountsListTemp.firstOrNull()
        userProfile?.let {
            fragmentStudentAttendanceScreenBinding.presentValueTv.text =
                it.total_present_count.toString()
            fragmentStudentAttendanceScreenBinding.absentValueTv.text =
                it.total_not_present_count.toString()
            fragmentStudentAttendanceScreenBinding.totalValueTv.text = it.total_count.toString()
            totalStudentsPresentList.addAll(it.present_list)
            totalStudentsAbsentList.addAll(it.not_present_list)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleDegreeData(departmentData: CommonResponseModel<ProfessorStudentReportModel>?) {
        CommonUtility.cancelProgressDialog(activity)
        departmentData?.data?.let { reportData ->
            val uniqueDegrees = reportData.map { it.degree }.distinct()
            val updatedDegreeValues = mutableListOf("Select").apply {
                addAll(uniqueDegrees)
            }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                updatedDegreeValues
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            fragmentStudentAttendanceScreenBinding.degreeSpinner.adapter = adapter
            fragmentStudentAttendanceScreenBinding.professorNameValueTv.text =
                SharedPref.getFirstName(context) + " " + SharedPref.getLastName(context)
            fragmentStudentAttendanceScreenBinding.todayDateValueTv.text =
                CommonUtility.getCurrentDate(context)
        }
    }

    private fun updateDepartmentSpinner(selectedDegree: String) {
        studentAttendanceFragmentScreenViewModel.getDropDownValuesData()?.value?.data?.let { reportData ->
            val uniqueDepartments = reportData
                .filter { it.degree == selectedDegree }
                .map { it.departement }
                .distinct()
            val updatedDepartments = mutableListOf("Select").apply {
                addAll(uniqueDepartments)
            }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                updatedDepartments
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            fragmentStudentAttendanceScreenBinding.departmentSpinner.adapter = adapter
        }
    }

    private fun updateSemesterSpinner(selectedDegree: String, selectedDepartment: String) {
        studentAttendanceFragmentScreenViewModel.getDropDownValuesData()?.value?.data?.let { reportData ->
            val uniqueSemesters = reportData
                .filter { it.degree == selectedDegree && it.departement == selectedDepartment }
                .map { it.semester }
                .distinct()
            val updatedSemesters = mutableListOf("Select").apply {
                addAll(uniqueSemesters)
            }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                updatedSemesters
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            fragmentStudentAttendanceScreenBinding.semesterSpinner.adapter = adapter
        }
    }

    private fun updateSectionSpinner(
        selectedDegree: String,
        selectedDepartment: String,
        selectedSemester: String
    ) {
        studentAttendanceFragmentScreenViewModel.getDropDownValuesData()?.value?.data?.let { reportData ->
            val uniqueSections = reportData
                .filter { it.degree == selectedDegree && it.departement == selectedDepartment && it.semester == selectedSemester }
                .map { it.section }
                .distinct()
            val updatedSections = mutableListOf("Select").apply {
                addAll(uniqueSections)
            }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                updatedSections
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            fragmentStudentAttendanceScreenBinding.sectionSpinner.adapter = adapter
        }
    }

    private fun callStudentCountListApi() {
        studentAttendanceFragmentScreenViewModel.callStudentCountListApi(
            requireActivity(),
            "student_counts",
            selectedDepartment,
            selectedSection,
            selectedSemester,
            selectedDegree
        )
    }

    private fun handleStudentListData(
        studentsData: CommonResponseModel<StudentListBasedModel>?,
        popupRecyclerView: RecyclerView
    ) {
        studentsList.clear()
        studentsData?.let {
            val filteredItems = it.data.filter { students ->
                students.is_status == "1" && students.is_deleted == "0"
            }
            studentsList.addAll(filteredItems)
            setBindingAdapter(studentsList, popupRecyclerView)
        }
    }

    private fun showPopup(v: View) {
        val inflater = LayoutInflater.from(context)
        val popupBinding: ShowAttendanceListPopUpBinding =
            DataBindingUtil.inflate(inflater, R.layout.show_attendance_list_pop_up, null, false)
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
        popupBinding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }
            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }
            override fun afterTextChanged(editable: Editable?) {
                if (attendanceStudentsListAdapter != null) {
                    val searchText = popupBinding.etSearch.text.toString().lowercase(
                        Locale.getDefault()
                    )
                    attendanceStudentsListAdapter!!.filter(searchText)
                }
            }
        })

        CommonUtility.showProgressDialog(context)
        studentAttendanceFragmentScreenViewModel.callStudentListApi(
            requireActivity(),
            "studentlist_basedon_user_id_degree_depart_semester_section",
            selectedDegree,
            selectedDepartment,
            selectedSemester,
            selectedSection
        )
        observeStudentViewModel(studentAttendanceFragmentScreenViewModel, popupBinding.recyclerView)

        popupBinding.updateButton.setOnClickListener {
            if (attendanceStudentsListAdapter?.totalStudentCountPresentList()!!.isNotEmpty()) {
                val commaSeparatedTotalStudentIds =
                    attendanceStudentsListAdapter?.totalStudentCountPresentList()!!
                        .joinToString(",")
                val commaSeparatedPresentAbsentStudentIds =
                    attendanceStudentsListAdapter?.studentCountPresentAbsentList()!!
                        .joinToString(",")
                callMarkAttendanceApi(commaSeparatedTotalStudentIds,commaSeparatedPresentAbsentStudentIds)
                observeStudentAttendanceViewModel(
                    studentAttendanceFragmentScreenViewModel,
                    popupWindow
                )
            } else {
                CommonUtility.toastString("Please mark attendance..!", activity)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setBindingAdapter(
        studentsModel: List<StudentListBasedModel>,
        popupRecyclerView: RecyclerView
    ) {
        attendanceStudentsListAdapter = AttendanceStudentsListAdapter(context, studentsModel)
        popupRecyclerView.adapter = attendanceStudentsListAdapter
        attendanceStudentsListAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(context)
    }

    private fun observeStudentViewModel(
        viewModel: StudentAttendanceFragmentScreenViewModel,
        popupRecyclerView: RecyclerView) {
        viewModel.getStudentListData()?.observe(requireActivity(), Observer { studentList ->
            if (studentList != null) {
                if (studentList.status == 400 && studentList.data.isNotEmpty())
                {
                    CommonUtility.cancelProgressDialog(activity)
                }else{
                    handleStudentListData(studentList, popupRecyclerView)
                }
            } else {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
    }

    private fun callMarkAttendanceApi(totalStudentsId: String,selectedPresentAbsentStudentsId: String) {
        CommonUtility.showProgressDialog(context)
        studentAttendanceFragmentScreenViewModel.callMarkStudentAttendanceApi(
            requireActivity(), "professor_mark_attendence_student",
            SharedPref.getId(context)!!, totalStudentsId,selectedPresentAbsentStudentsId
        )
    }

    private fun observeStudentAttendanceViewModel(
        viewModel: StudentAttendanceFragmentScreenViewModel,
        popupWindow: PopupWindow
    ) {
        viewModel.getMarkStudentAttendanceApiData()
            ?.observe(requireActivity(), Observer { studentAttendance ->
                if (studentAttendance != null) {
                    if (studentAttendance.status == 403 && studentAttendance.data.isNotEmpty()) {
                        CommonUtility.cancelProgressDialog(activity)
                    } else {
                        popupWindow.dismiss()
                        CommonUtility.toastString("Attendance Marked Successfully..!", activity)
                        callStudentCountListApi()
                        observeViewModel(studentAttendanceFragmentScreenViewModel, 2)
                    }
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showPopupStudentsList(v: View, screenName: String, studentsList: List<String>) {
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
        nameListAdapter = NameListAdapter(context, studentsList)
        popupBinding.recyclerView.adapter = nameListAdapter
        nameListAdapter!!.notifyDataSetChanged()
    }
}