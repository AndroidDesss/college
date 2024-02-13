package com.desss.collegeproduct.module.managementSubModule.studentDetail.fragment

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
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.databinding.FragmentStudentDetailScreenBinding
import com.desss.collegeproduct.module.managementSubModule.studentDetail.adapter.StudentsListManagementAdapter
import com.desss.collegeproduct.module.managementSubModule.studentDetail.model.DegreeDepartmentSectionModel
import com.desss.collegeproduct.module.managementSubModule.studentDetail.model.StudentListManagementBasedModel
import com.desss.collegeproduct.module.managementSubModule.studentDetail.viewmodel.StudentDetailScreenViewModel
import java.util.Locale


class StudentDetailFragmentScreen : Fragment() {

    private lateinit var fragmentStudentDetailScreenBinding: FragmentStudentDetailScreenBinding

    private lateinit var studentDetailScreenViewModel: StudentDetailScreenViewModel

    private lateinit var selectedDegree: String

    private lateinit var selectedDepartment: String

    private lateinit var selectedSemester: String

    private lateinit var selectedSection: String

    private var studentsManagementList = arrayListOf<StudentListManagementBasedModel>()

    private var studentsListManagementAdapter: StudentsListManagementAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentStudentDetailScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_student_detail_screen, container, false)
        initViewModel()
        callApi()
        observeViewModel(studentDetailScreenViewModel, 1)
        initListener()

        return fragmentStudentDetailScreenBinding.root

    }

    private fun initListener() {
        fragmentStudentDetailScreenBinding.btnViewStudentList.setOnClickListener(onClickListener)
        fragmentStudentDetailScreenBinding.degreeSpinner.onItemSelectedListener = onItemSelectedListener
        fragmentStudentDetailScreenBinding.departmentSpinner.onItemSelectedListener = onItemSelectedListener
        fragmentStudentDetailScreenBinding.semesterSpinner.onItemSelectedListener = onItemSelectedListener
        fragmentStudentDetailScreenBinding.sectionSpinner.onItemSelectedListener = onItemSelectedListener

        fragmentStudentDetailScreenBinding.etSearch.addTextChangedListener(object : TextWatcher {
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
                if (studentsListManagementAdapter != null) {
                    val searchText = fragmentStudentDetailScreenBinding.etSearch.text.toString()
                        .toLowerCase(Locale.getDefault())
                    studentsListManagementAdapter!!.filter(searchText)
                }
            }
        })
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btnViewStudentList ->
                if (selectedDegree != "Select" || selectedDepartment != "Select" || selectedSemester != "Select" || selectedSection != "Select") {
                    CommonUtility.showProgressDialog(context)
                    studentDetailScreenViewModel.callStudentListApi(
                        requireActivity(),
                        "studentlist_basedon_user_id_degree_depart_semester_section",
                        selectedDegree,
                        selectedDepartment,
                        selectedSemester,
                        selectedSection
                    )
                    observeViewModel(studentDetailScreenViewModel, 2)
                } else {
                    CommonUtility.toastString("Please select categories..!", activity)
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
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    private fun initViewModel() {
        studentDetailScreenViewModel =
            StudentDetailScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        studentDetailScreenViewModel.callDropDownValuesApi(
            requireActivity(),
            "read",
            "professor_departments",
        )
    }

    private fun observeViewModel(viewModel: StudentDetailScreenViewModel, position: Int) {
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
                        fragmentStudentDetailScreenBinding.rlError.visibility = View.VISIBLE
                    } else {
                        fragmentStudentDetailScreenBinding.rlError.visibility = View.GONE
                        handleStudentListData(studentList)
                    }
                } else {
                    CommonUtility.cancelProgressDialog(activity)
                    fragmentStudentDetailScreenBinding.rlError.visibility = View.VISIBLE
                }
            })
        }
    }


    private fun handleDegreeData(departmentData: CommonResponseModel<DegreeDepartmentSectionModel>?) {
        CommonUtility.cancelProgressDialog(activity)
        departmentData?.data?.let { reportData ->
            val uniqueDegrees = reportData.map { it.degree!! }.distinct()
            val updatedDegreeValues = mutableListOf("Select").apply {
                addAll(uniqueDegrees)
            }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                updatedDegreeValues
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            fragmentStudentDetailScreenBinding.degreeSpinner.adapter = adapter
        }
    }

    private fun updateDepartmentSpinner(selectedDegree: String) {
        studentDetailScreenViewModel.getDropDownValuesData()?.value?.data?.let { reportData ->
            val uniqueDepartments = reportData
                .filter { it.degree == selectedDegree }
                .map { it.departement!! }
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
            fragmentStudentDetailScreenBinding.departmentSpinner.adapter = adapter
        }
    }

    private fun updateSemesterSpinner(selectedDegree: String, selectedDepartment: String) {
        studentDetailScreenViewModel.getDropDownValuesData()?.value?.data?.let { reportData ->
            val uniqueSemesters = reportData
                .filter { it.degree == selectedDegree && it.departement == selectedDepartment }
                .map { it.semester!! }
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
            fragmentStudentDetailScreenBinding.semesterSpinner.adapter = adapter
        }
    }

    private fun updateSectionSpinner(
        selectedDegree: String,
        selectedDepartment: String,
        selectedSemester: String
    ) {
        studentDetailScreenViewModel.getDropDownValuesData()?.value?.data?.let { reportData ->
            val uniqueSections = reportData
                .filter { it.degree == selectedDegree && it.departement == selectedDepartment && it.semester == selectedSemester }
                .map { it.section!! }
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
            fragmentStudentDetailScreenBinding.sectionSpinner.adapter = adapter
        }
    }

    private fun handleStudentListData(studentsData: CommonResponseModel<StudentListManagementBasedModel>?) {
        studentsManagementList.clear()
        studentsData?.let {
            val filteredItems = it.data.filter { students ->
                students.is_status == "1" && students.is_deleted == "0"
            }
            studentsManagementList.addAll(filteredItems)
            setBindingAdapter(studentsManagementList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setBindingAdapter(studentsModel: List<StudentListManagementBasedModel>) {
        studentsListManagementAdapter =
            StudentsListManagementAdapter(context, studentsModel)
        fragmentStudentDetailScreenBinding.recyclerView.adapter = studentsListManagementAdapter
        studentsListManagementAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(context)
    }


}