package com.desss.collegeproduct.module.professorSubModule.report.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.FragmentReportScreenBinding
import com.desss.collegeproduct.module.professorSubModule.report.adapter.StudentsListAdapter
import com.desss.collegeproduct.module.professorSubModule.report.model.ProfessorStudentReportModel
import com.desss.collegeproduct.module.professorSubModule.report.model.StudentListBasedModel
import com.desss.collegeproduct.module.professorSubModule.report.viewmodel.ReportFragmentScreenViewModel
import java.util.Locale

class ReportFragmentScreen : Fragment() {

    private lateinit var fragmentReportScreenBinding: FragmentReportScreenBinding

    private lateinit var reportFragmentScreenViewModel: ReportFragmentScreenViewModel

    private lateinit var selectedDegree: String

    private lateinit var selectedDepartment: String

    private lateinit var selectedSemester: String

    private lateinit var selectedSection: String

    private var studentsList = arrayListOf<StudentListBasedModel>()

    private var studentsListAdapter: StudentsListAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentReportScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_report_screen, container, false)
        initViewModel()
        callApi()
        observeViewModel(reportFragmentScreenViewModel, 1)
        initListener()
        return fragmentReportScreenBinding.root
    }

    private fun initListener() {
        fragmentReportScreenBinding.btnViewStudentList.setOnClickListener(onClickListener)
        fragmentReportScreenBinding.viewReportTv.setOnClickListener(onClickListener)
        fragmentReportScreenBinding.degreeSpinner.onItemSelectedListener = onItemSelectedListener
        fragmentReportScreenBinding.departmentSpinner.onItemSelectedListener =
            onItemSelectedListener
        fragmentReportScreenBinding.semesterSpinner.onItemSelectedListener = onItemSelectedListener
        fragmentReportScreenBinding.sectionSpinner.onItemSelectedListener = onItemSelectedListener

        fragmentReportScreenBinding.etSearch.addTextChangedListener(object : TextWatcher {
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
                if (studentsListAdapter != null) {
                    val searchText = fragmentReportScreenBinding.etSearch.text.toString()
                        .toLowerCase(Locale.getDefault())
                    studentsListAdapter!!.filter(searchText)
                }
            }
        })
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
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.viewReportTv -> {
                val viewReportFragmentScreen = ViewReportFragmentScreen()
                CommonUtility.navigateToFragment(
                    (context as FragmentActivity).supportFragmentManager,
                    viewReportFragmentScreen,
                    R.id.container,
                    true
                )
            }

            R.id.btnViewStudentList ->
                if (selectedDegree != "Select" || selectedDepartment != "Select" || selectedSemester != "Select" || selectedSection != "Select") {
                    CommonUtility.showProgressDialog(context)
                    reportFragmentScreenViewModel.callStudentListApi(
                        requireActivity(),
                        "studentlist_basedon_user_id_degree_depart_semester_section",
                        selectedDegree,
                        selectedDepartment,
                        selectedSemester,
                        selectedSection
                    )
                    observeViewModel(reportFragmentScreenViewModel, 2)
                } else {
                    CommonUtility.toastString("Please select categories..!", activity)
                }
        }
    }

    private fun initViewModel() {
        reportFragmentScreenViewModel =
            ReportFragmentScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        reportFragmentScreenViewModel.callDropDownValuesApi(
            requireActivity(),
            "read",
            "professor_departments",
            SharedPref.getId(context).toString()
        )
    }

    private fun observeViewModel(viewModel: ReportFragmentScreenViewModel, position: Int) {
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
        } else {
            viewModel.getStudentListData()?.observe(requireActivity(), Observer { studentList ->
                if (studentList != null) {
                    if (studentList.status == 400 && studentList.data.isNotEmpty()) {
                        CommonUtility.cancelProgressDialog(activity)
                        fragmentReportScreenBinding.rlError.visibility = View.VISIBLE
                    } else {
                        fragmentReportScreenBinding.rlError.visibility = View.GONE
                        handleStudentListData(studentList)
                    }
                } else {
                    CommonUtility.cancelProgressDialog(activity)
                    fragmentReportScreenBinding.rlError.visibility = View.VISIBLE
                }
            })
        }
    }

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
            fragmentReportScreenBinding.degreeSpinner.adapter = adapter
        }
    }

    private fun updateDepartmentSpinner(selectedDegree: String) {
        reportFragmentScreenViewModel.getDropDownValuesData()?.value?.data?.let { reportData ->
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
            fragmentReportScreenBinding.departmentSpinner.adapter = adapter
        }
    }

    private fun updateSemesterSpinner(selectedDegree: String, selectedDepartment: String) {
        reportFragmentScreenViewModel.getDropDownValuesData()?.value?.data?.let { reportData ->
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
            fragmentReportScreenBinding.semesterSpinner.adapter = adapter
        }
    }

    private fun updateSectionSpinner(
        selectedDegree: String,
        selectedDepartment: String,
        selectedSemester: String
    ) {
        reportFragmentScreenViewModel.getDropDownValuesData()?.value?.data?.let { reportData ->
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
            fragmentReportScreenBinding.sectionSpinner.adapter = adapter
        }
    }

    private fun handleStudentListData(studentsData: CommonResponseModel<StudentListBasedModel>?) {
        studentsList.clear()
        studentsData?.let {
            val filteredItems = it.data.filter { students ->
                students.is_status == "1" && students.is_deleted == "0"
            }
            studentsList.addAll(filteredItems)
            setBindingAdapter(studentsList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setBindingAdapter(studentsModel: List<StudentListBasedModel>) {
        studentsListAdapter =
            StudentsListAdapter(context, studentsModel, reportFragmentScreenViewModel)
        fragmentReportScreenBinding.recyclerView.adapter = studentsListAdapter
        studentsListAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(context)
    }

}