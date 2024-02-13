package com.desss.collegeproduct.module.professorSubModule.schedule.fragment

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.CommonValidation
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.AddSchedulePopupBinding
import com.desss.collegeproduct.databinding.FragmentScheduleScreenBinding
import com.desss.collegeproduct.module.professorSubModule.report.model.ProfessorStudentReportModel
import com.desss.collegeproduct.module.professorSubModule.schedule.adapter.ViewScheduleAdapter
import com.desss.collegeproduct.module.professorSubModule.schedule.model.PostScheduleModel
import com.desss.collegeproduct.module.professorSubModule.schedule.model.ViewScheduleModel
import com.desss.collegeproduct.module.professorSubModule.schedule.viewmodel.ScheduleFragmentScreenViewModel
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import java.util.Locale

class ScheduleFragmentScreen : Fragment() {

    private lateinit var fragmentScheduleScreenBinding: FragmentScheduleScreenBinding

    private lateinit var scheduleFragmentScreenViewModel: ScheduleFragmentScreenViewModel

    private lateinit var selectedDegree: String

    private lateinit var selectedDepartment: String

    private lateinit var selectedSemester: String

    private lateinit var selectedSection: String

    private lateinit var selectedDate: String

    private var scheduleList = arrayListOf<ViewScheduleModel>()

    private var viewScheduleAdapter: ViewScheduleAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentScheduleScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_screen, container, false)
        selectedDate = CommonUtility.getCurrentDate(context)
        initViewModel()
        initListener()
        callScheduleApi()
        return fragmentScheduleScreenBinding.root
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        scheduleFragmentScreenViewModel.callDropDownValuesApi(
            requireActivity(),
            "read",
            "professor_departments",
            SharedPref.getId(context).toString()
        )
    }

    fun callScheduleApi() {
        CommonUtility.showProgressDialog(context)
        scheduleFragmentScreenViewModel.callScheduleApi(
            requireActivity(),
            "read",
            "schedules",
            SharedPref.getId(context).toString()
        )
        observeScheduleViewModel(scheduleFragmentScreenViewModel)
    }

    private fun initViewModel() {
        scheduleFragmentScreenViewModel =
            ScheduleFragmentScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun initListener() {
        fragmentScheduleScreenBinding.btnAdd.setOnClickListener(onClickListener)
        fragmentScheduleScreenBinding.simpleCalendarView.setOnDateChangeListener(
            onDateChangeListener
        )
        fragmentScheduleScreenBinding.etSearch.addTextChangedListener(object : TextWatcher {
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
                if (viewScheduleAdapter != null) {
                    val searchText =
                        fragmentScheduleScreenBinding.etSearch.text.toString().toLowerCase(
                            Locale.getDefault()
                        )
                    viewScheduleAdapter!!.filter(searchText)
                }
            }
        })
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btnAdd -> {
                showPopup(view)
            }
        }
    }

    private val onDateChangeListener =
        CalendarView.OnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth-${month + 1}-$year"
        }

    @SuppressLint("SetTextI18n")
    private fun showPopup(v: View) {
        val inflater = LayoutInflater.from(context)
        val popupBinding: AddSchedulePopupBinding =
            DataBindingUtil.inflate(inflater, R.layout.add_schedule_popup, null, false)
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
        callApi()
        observeViewModel(scheduleFragmentScreenViewModel, 1, popupBinding, popupWindow)

        popupBinding.etFromTime.setOnClickListener {
            showTimePickerDialog(requireContext(), popupBinding.etFromTime)
        }

        popupBinding.etToTime.setOnClickListener {
            showTimePickerDialog(requireContext(), popupBinding.etToTime)
        }

        popupBinding.degreeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    popupViewItem: View?,
                    popupPosition: Int,
                    popupId: Long
                ) {
                    selectedDegree = parent.getItemAtPosition(popupPosition).toString()
                    updateDepartmentSpinner(selectedDegree, popupBinding)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }

        popupBinding.departmentSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    popupViewItem: View?,
                    popupPosition: Int,
                    popupId: Long
                ) {
                    selectedDepartment = parent.getItemAtPosition(popupPosition).toString()
                    updateSemesterSpinner(selectedDegree, selectedDepartment, popupBinding)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }

        popupBinding.semesterSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    popupViewItem: View?,
                    popupPosition: Int,
                    popupId: Long
                ) {
                    selectedSemester = parent.getItemAtPosition(popupPosition).toString()
                    updateSectionSpinner(
                        selectedDegree,
                        selectedDepartment,
                        selectedSemester,
                        popupBinding
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

        popupBinding.sectionSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    popupViewItem: View?,
                    popupPosition: Int,
                    popupId: Long
                ) {
                    selectedSection = parent.getItemAtPosition(popupPosition).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }

        popupBinding.btnSave.setOnClickListener {
            if (selectedDegree == "Select" || selectedDepartment == "Select" || selectedSemester == "Select" || selectedSection == "Select") {
                CommonUtility.toastString("Please select categories..!", activity)
            } else if (!CommonValidation.isEditTextNotEmpty(popupBinding.etFromTime)) {
                popupBinding.etFromTime.error = "Enter From Time"
            } else if (!CommonValidation.isEditTextNotEmpty(popupBinding.etToTime)) {
                popupBinding.etToTime.error = "Enter To Time"
            } else if (!CommonValidation.isEditTextNotEmpty(popupBinding.inputMessage)) {
                popupBinding.inputMessage.error = "Enter Schedule Message"
            } else {
                CommonUtility.showProgressDialog(context)
                scheduleFragmentScreenViewModel.postScheduleMessage(
                    requireActivity(),
                    "professor_schedules",
                    SharedPref.getId(context).toString(),
                    selectedDegree,
                    selectedDepartment,
                    selectedSection,
                    selectedSemester,
                    selectedDate,
                    popupBinding.etFromTime.text.toString(),
                    popupBinding.etToTime.text.toString(),
                    popupBinding.inputMessage.text.toString()
                )
                observeViewModel(scheduleFragmentScreenViewModel, 2, popupBinding, popupWindow)
            }
        }
    }

    private fun observeViewModel(
        viewModel: ScheduleFragmentScreenViewModel,
        position: Int,
        popupBinding: AddSchedulePopupBinding,
        popupWindow: PopupWindow
    ) {
        if (position == 1) {
            viewModel.getDropDownValuesData()
                ?.observe(requireActivity(), Observer { departmentData ->
                    if (departmentData != null) {
                        if (departmentData.status == 403 && departmentData.data.isNotEmpty()) {
                            CommonUtility.cancelProgressDialog(activity)
                        } else {
                            handleDegreeData(departmentData, popupBinding)
                        }
                    }
                })
        } else if (position == 2) {
            viewModel.getPostScheduleMessageData()
                ?.observe(requireActivity(), Observer { postMessageData ->
                    if (postMessageData != null) {
                        if (postMessageData.status == 403 && postMessageData.data.isNotEmpty()) {
                            CommonUtility.cancelProgressDialog(activity)
                        } else {
                            val postScheduleList: List<PostScheduleModel> = postMessageData.data
                            val userProfile: PostScheduleModel? = postScheduleList.firstOrNull()
                            userProfile?.let {
                                if (it.msg == "Inserted Successfully") {
                                    CommonUtility.toastString(
                                        "Schedule Inserted Successfully..!",
                                        activity
                                    )
                                    popupWindow.dismiss()
                                    callScheduleApi()
                                }
                            }
                        }
                    }
                })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleDegreeData(
        departmentData: CommonResponseModel<ProfessorStudentReportModel>?,
        popupBinding: AddSchedulePopupBinding
    ) {
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
            popupBinding.degreeSpinner.adapter = adapter
        }
    }

    private fun updateDepartmentSpinner(
        selectedDegree: String,
        popupBinding: AddSchedulePopupBinding
    ) {
        scheduleFragmentScreenViewModel.getDropDownValuesData()?.value?.data?.let { reportData ->
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
            popupBinding.departmentSpinner.adapter = adapter
        }
    }

    private fun updateSemesterSpinner(
        selectedDegree: String,
        selectedDepartment: String,
        popupBinding: AddSchedulePopupBinding
    ) {
        scheduleFragmentScreenViewModel.getDropDownValuesData()?.value?.data?.let { reportData ->
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
            popupBinding.semesterSpinner.adapter = adapter
        }
    }

    private fun updateSectionSpinner(
        selectedDegree: String,
        selectedDepartment: String,
        selectedSemester: String,
        popupBinding: AddSchedulePopupBinding
    ) {
        scheduleFragmentScreenViewModel.getDropDownValuesData()?.value?.data?.let { reportData ->
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
            popupBinding.sectionSpinner.adapter = adapter
        }
    }

    @SuppressLint("SetTextI18n")
    fun showTimePickerDialog(context: Context, editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val seconds = ":00"
        val timePickerDialog = TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                editText.setText(formattedTime + seconds)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun observeScheduleViewModel(viewModel: ScheduleFragmentScreenViewModel) {
        viewModel.getScheduleApiValuesData()
            ?.observe(requireActivity(), Observer { viewScheduleData ->
                if (viewScheduleData != null) {
                    if (viewScheduleData.status == 403 && viewScheduleData.data.isNotEmpty()) {
                        CommonUtility.cancelProgressDialog(activity)
                        fragmentScheduleScreenBinding.rlError.visibility = View.VISIBLE
                    } else {
                        handleViewScheduleData(viewScheduleData)
                        fragmentScheduleScreenBinding.rlError.visibility = View.GONE
                    }
                } else {
                    CommonUtility.cancelProgressDialog(activity)
                    fragmentScheduleScreenBinding.rlError.visibility = View.VISIBLE
                }
            })
    }

    private fun handleViewScheduleData(viewScheduleData: CommonResponseModel<ViewScheduleModel>?) {
        scheduleList.clear()
        viewScheduleData?.let {
            val filteredItems = it.data.filter { schedule ->
                schedule.status == "1" && schedule.is_deleted == "0"
            }
            scheduleList.addAll(filteredItems)
            setBindingAdapter(scheduleList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setBindingAdapter(notificationModel: List<ViewScheduleModel>) {
        viewScheduleAdapter =
            ViewScheduleAdapter(this, context, notificationModel, scheduleFragmentScreenViewModel)
        fragmentScheduleScreenBinding.recyclerView.adapter = viewScheduleAdapter
        viewScheduleAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(context)
    }

}


