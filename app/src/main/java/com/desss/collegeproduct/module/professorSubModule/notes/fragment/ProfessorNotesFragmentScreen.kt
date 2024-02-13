package com.desss.collegeproduct.module.professorSubModule.notes.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.CommonValidation
import com.desss.collegeproduct.commonfunctions.Constants
import com.desss.collegeproduct.commonfunctions.FileUtils
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.FragmentProfessorNotesScreenBinding
import com.desss.collegeproduct.module.professorSubModule.notes.viewmodel.ProfessorNotesFragmentScreenViewModel
import com.desss.collegeproduct.module.professorSubModule.report.model.ProfessorStudentReportModel


class ProfessorNotesFragmentScreen : Fragment() {
    private lateinit var fragmentProfessorNotesScreenBinding: FragmentProfessorNotesScreenBinding

    private lateinit var professorNotesFragmentScreenViewModel: ProfessorNotesFragmentScreenViewModel

    private lateinit var selectedDegree: String

    private lateinit var selectedDepartment: String

    private lateinit var selectedSemester: String

    private lateinit var selectedSection: String

    private lateinit var filePath: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProfessorNotesScreenBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_professor_notes_screen,
            container,
            false
        )
        initViewModel()
        callApi()
        observeViewModel(professorNotesFragmentScreenViewModel, 1)
        initListener()
        return fragmentProfessorNotesScreenBinding.root
    }

    private fun initListener() {
        fragmentProfessorNotesScreenBinding.btnChooseFile.setOnClickListener(onClickListener)
        fragmentProfessorNotesScreenBinding.removeIcon.setOnClickListener(onClickListener)
        fragmentProfessorNotesScreenBinding.btnUploadFile.setOnClickListener(onClickListener)
        fragmentProfessorNotesScreenBinding.degreeSpinner.onItemSelectedListener =
            onItemSelectedListener
        fragmentProfessorNotesScreenBinding.departmentSpinner.onItemSelectedListener =
            onItemSelectedListener
        fragmentProfessorNotesScreenBinding.semesterSpinner.onItemSelectedListener =
            onItemSelectedListener
        fragmentProfessorNotesScreenBinding.sectionSpinner.onItemSelectedListener =
            onItemSelectedListener
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btnChooseFile -> {
                pickPdfFile()
            }

            R.id.removeIcon -> {
                CommonUtility.showAlertDialog(context,
                    "",
                    "Are you sure you want to delete the file ?",
                    "Yes",
                    "No",
                    object : CommonUtility.DialogClickListener {
                        override fun dialogOkBtnClicked(value: String?) {
                            filePath = ""
                            fragmentProfessorNotesScreenBinding.fileInfoCardView.visibility =
                                View.GONE
                            fragmentProfessorNotesScreenBinding.btnUploadFile.visibility = View.GONE
                            fragmentProfessorNotesScreenBinding.fileNameTv.text = ""
                        }

                        override fun dialogNoBtnClicked(value: String?) {}
                    }
                )
            }

            R.id.btnUploadFile ->
                if (selectedDegree == "Select" || selectedDepartment == "Select" || selectedSemester == "Select" || selectedSection == "Select") {
                    CommonUtility.toastString("Please select categories..!", activity)
                } else if (filePath == "") {
                    CommonUtility.toastString("Please select file to upload..!", activity)
                } else if (!CommonValidation.isEditTextNotEmpty(fragmentProfessorNotesScreenBinding.etFileName)) {
                    fragmentProfessorNotesScreenBinding.etFileName.error = "Enter FileName"
                } else {
                    uploadPdf()
                }
        }
    }

    private fun uploadPdf() {
        CommonUtility.showProgressDialog(context)
        professorNotesFragmentScreenViewModel.callPdfUploadValuesApi(
            requireActivity(),
            "professor_add_notes",
            SharedPref.getId(requireContext()).toString(),
            selectedDegree,
            selectedDepartment,
            selectedSection,
            selectedSemester,
            fragmentProfessorNotesScreenBinding.etFileName.text.toString(),
            filePath
        )
        observeViewModel(professorNotesFragmentScreenViewModel, 2)
    }

    private fun pickPdfFile() {
        val filePickerIntent = FileUtils.createPdfFilePickerIntent()
        startActivityForResult(filePickerIntent, 1)
    }

    @SuppressLint("SetTextI18n")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.PICK_PDF_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->

                filePath = getFilePathFromUri(uri).toString()
                val filename = filePath.substring(filePath.lastIndexOf("/") + 1)
                fragmentProfessorNotesScreenBinding.fileInfoCardView.visibility = View.VISIBLE
                fragmentProfessorNotesScreenBinding.btnUploadFile.visibility = View.VISIBLE
                fragmentProfessorNotesScreenBinding.fileNameTv.text = "$filename.pdf"
            }
        }
    }

    private fun getFilePathFromUri(uri: Uri): String? {
        val documentFile = DocumentFile.fromSingleUri(requireContext(), uri)
        return documentFile?.uri?.path
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
        professorNotesFragmentScreenViewModel =
            ProfessorNotesFragmentScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        professorNotesFragmentScreenViewModel.callDropDownValuesApi(
            requireActivity(), "read", "professor_departments",
            SharedPref.getId(context).toString()
        )
    }

    private fun observeViewModel(viewModel: ProfessorNotesFragmentScreenViewModel, position: Int) {
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
            viewModel.getPdfUploadValuesData()
                ?.observe(requireActivity(), Observer { uploadedData ->
                    if (uploadedData != null) {
                        if (uploadedData.status == 403 && uploadedData.data.isNotEmpty()) {
                            CommonUtility.cancelProgressDialog(activity)
                            CommonUtility.toastString("Failed to Upload..!", activity)
                        } else {
                            CommonUtility.cancelProgressDialog(activity)
                            filePath = ""
                            fragmentProfessorNotesScreenBinding.fileInfoCardView.visibility =
                                View.GONE
                            fragmentProfessorNotesScreenBinding.btnUploadFile.visibility = View.GONE
                            fragmentProfessorNotesScreenBinding.fileNameTv.text = ""
                            fragmentProfessorNotesScreenBinding.etFileName.setText("")
                            CommonUtility.toastString("Successfully Uploaded..!", activity)
                        }
                    } else {
                        CommonUtility.cancelProgressDialog(activity)
                        CommonUtility.toastString("Failed to Upload..!", activity)
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
            fragmentProfessorNotesScreenBinding.degreeSpinner.adapter = adapter
        }
    }

    private fun updateDepartmentSpinner(selectedDegree: String) {
        professorNotesFragmentScreenViewModel.getDropDownValuesData()?.value?.data?.let { reportData ->
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
            fragmentProfessorNotesScreenBinding.departmentSpinner.adapter = adapter
        }
    }

    private fun updateSemesterSpinner(selectedDegree: String, selectedDepartment: String) {
        professorNotesFragmentScreenViewModel.getDropDownValuesData()?.value?.data?.let { reportData ->
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
            fragmentProfessorNotesScreenBinding.semesterSpinner.adapter = adapter
        }
    }

    private fun updateSectionSpinner(
        selectedDegree: String,
        selectedDepartment: String,
        selectedSemester: String
    ) {
        professorNotesFragmentScreenViewModel.getDropDownValuesData()?.value?.data?.let { reportData ->
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
            fragmentProfessorNotesScreenBinding.sectionSpinner.adapter = adapter
        }
    }
}