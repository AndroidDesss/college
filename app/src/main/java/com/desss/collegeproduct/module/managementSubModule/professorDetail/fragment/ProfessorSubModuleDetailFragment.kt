package com.desss.collegeproduct.module.managementSubModule.professorDetail.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.databinding.FragmentProfessorSubModuleDetailBinding
import com.desss.collegeproduct.databinding.ManagementAttendanceRecapPopUpLayoutBinding
import com.desss.collegeproduct.databinding.ManagementProfessorProfilePopUpLayoutBinding
import com.desss.collegeproduct.databinding.ManagementScheduleListPopUpBindingBinding
import com.desss.collegeproduct.module.commonDashBoardFragment.profile.model.ProfileModel
import com.desss.collegeproduct.module.managementSubModule.professorDetail.adapter.ManagementViewScheduleAdapter
import com.desss.collegeproduct.module.managementSubModule.professorDetail.viewmodel.ProfessorSubModuleDetailScreenViewModel
import com.desss.collegeproduct.module.professorSubModule.professorAttendance.model.ProfessorCountModel
import com.desss.collegeproduct.module.professorSubModule.schedule.model.ViewScheduleModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class ProfessorSubModuleDetailFragment : Fragment() {

    private lateinit var fragmentProfessorSubModuleDetailFragment: FragmentProfessorSubModuleDetailBinding

    private lateinit var professorSubModuleDetailScreenViewModel: ProfessorSubModuleDetailScreenViewModel

    private lateinit var commonProfessorId: String

    private var scheduleList = arrayListOf<ViewScheduleModel>()

    private var managementViewScheduleAdapter: ManagementViewScheduleAdapter? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProfessorSubModuleDetailFragment =
            DataBindingUtil.inflate(inflater, R.layout.fragment_professor_sub_module_detail, container, false)
        getBundleData()
        initViewModel()
        initListener()
        return fragmentProfessorSubModuleDetailFragment.root
    }

    fun getBundleData() {
        val bundle = arguments
        if (bundle != null) {
            commonProfessorId = bundle.getString("professorId").toString()
        }
    }

    private fun initViewModel() {
        professorSubModuleDetailScreenViewModel =
            ProfessorSubModuleDetailScreenViewModel(activity?.application!!, requireActivity())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListener() {
        fragmentProfessorSubModuleDetailFragment.profileDetailCardView.setOnClickListener(onClickListener)
        fragmentProfessorSubModuleDetailFragment.attendanceRecapDetailCardView.setOnClickListener(onClickListener)
        fragmentProfessorSubModuleDetailFragment.scheduleDetailCardView.setOnClickListener(onClickListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.profileDetailCardView -> {
                callProfessorProfileDetailApi()
                observeViewModel(view,professorSubModuleDetailScreenViewModel)
            }
            R.id.attendanceRecapDetailCardView -> {
                showProfessorAttendanceDetails(view)
            }
            R.id.scheduleDetailCardView -> {
                showProfessorScheduleDetails(view)
            }
        }
    }

    private fun callProfessorProfileDetailApi() {
        CommonUtility.showProgressDialog(context)
        professorSubModuleDetailScreenViewModel.callStudentProfileApi(
            requireActivity(),
            "read",
            "accounts_user",
            commonProfessorId
        )
    }

    private fun observeViewModel(view: View, viewModel: ProfessorSubModuleDetailScreenViewModel) {
        viewModel.getStudentProfileData()?.observe(requireActivity(), Observer { profileData ->
            showProfessorProfileList(view,profileData)
        })
    }

    private fun observeProfessorCountViewModel(popupBinding: ManagementAttendanceRecapPopUpLayoutBinding, viewModel: ProfessorSubModuleDetailScreenViewModel) {
        viewModel.getProfessorCountData()
            ?.observe(requireActivity(), Observer { professorAttendanceCountData ->
                if (professorAttendanceCountData != null) {
                    if (professorAttendanceCountData.status == 401 && professorAttendanceCountData.data.isNotEmpty()) {
                        CommonUtility.cancelProgressDialog(activity)
                        popupBinding.presentValueTv.text = "0"
                        popupBinding.absentValueTv.text = "0"
                        popupBinding.totalValueTv.text = "0"
                        popupBinding.lateValueTv.text = "0"
                    } else {
                        handleProfessorAttendanceCountData(popupBinding,professorAttendanceCountData)
                    }
                } else {
                    CommonUtility.cancelProgressDialog(activity)
                    popupBinding.presentValueTv.text = "0"
                    popupBinding.absentValueTv.text = "0"
                    popupBinding.totalValueTv.text = "0"
                    popupBinding.lateValueTv.text = "0"
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun handleProfessorAttendanceCountData(popupBinding: ManagementAttendanceRecapPopUpLayoutBinding,professorCountsData: CommonResponseModel<ProfessorCountModel>?) {
        CommonUtility.cancelProgressDialog(activity)
        val professorCountsListTemp: List<ProfessorCountModel> = professorCountsData!!.data
        val userProfile: ProfessorCountModel? = professorCountsListTemp.firstOrNull()
        userProfile?.let {
            popupBinding.presentValueTv.text = it.total_present_count.toString()
            popupBinding.absentValueTv.text = it.total_not_present_count.toString()
            popupBinding.totalValueTv.text = it.total_count.toString()
            popupBinding.lateValueTv.text = it.total_late_count.toString()
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun showProfessorProfileList(v: View, profileData: CommonResponseModel<ProfileModel>?) {
        CommonUtility.cancelProgressDialog(activity)
        val inflater = LayoutInflater.from(context)
        val popupBinding: ManagementProfessorProfilePopUpLayoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.management_professor_profile_pop_up_layout, null, false)
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
        if (profileData!!.status == 200) {
            val profileList: List<ProfileModel> = profileData.data
            val userProfile: ProfileModel? = profileList.firstOrNull()
            userProfile?.let {
                popupBinding.firstNameValueTv.text = it.first_name
                popupBinding.lastNameValueTv.text = it.last_name
                popupBinding.joinDateValueTv.text = it.admission_date
                popupBinding.birthDateValueTv.text = it.dob
                popupBinding.birthDateValueTv.text = it.dob
                popupBinding.emailIdValueTv.text = it.email
                popupBinding.mobileNumberValueTv.text = it.phone
                popupBinding.addressValueTv.text = it.address
            }
        }
    }

    private fun showProfessorAttendanceDetails(v: View) {
        val inflater = LayoutInflater.from(context)
        val popupBinding: ManagementAttendanceRecapPopUpLayoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.management_attendance_recap_pop_up_layout, null, false)
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
        getMonthDropDownValues(popupBinding)
        popupBinding.monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent!!.getItemAtPosition(position).toString() != "Select Month") {
                    val month = parent.getItemAtPosition(position).toString()
                    val subMonthYear = month.split(" ")
                    callProfessorCountApi(subMonthYear[0], subMonthYear[1])
                    observeProfessorCountViewModel(popupBinding,professorSubModuleDetailScreenViewModel)
                } else {
                    popupBinding.presentValueTv.text = "0"
                    popupBinding.absentValueTv.text = "0"
                    popupBinding.totalValueTv.text = "0"
                    popupBinding.lateValueTv.text = "0"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun callProfessorCountApi(month: String, year: String) {
        CommonUtility.showProgressDialog(context)
        professorSubModuleDetailScreenViewModel.callProfessorCountApi(
            requireActivity(),
            "professor_counts",
            commonProfessorId,
            month,
            year
        )
    }

    private fun getMonthDropDownValues(popupBinding: ManagementAttendanceRecapPopUpLayoutBinding) {
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
        popupBinding.monthSpinner.adapter = adapter
    }

    fun callScheduleApi(date: String) {
        CommonUtility.showProgressDialog(context)
        professorSubModuleDetailScreenViewModel.callScheduleApi(
            requireActivity(),
            "read",
            "schedules",
            commonProfessorId,
            date
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showProfessorScheduleDetails(v: View) {
        val inflater = LayoutInflater.from(context)
        val popupBinding: ManagementScheduleListPopUpBindingBinding =
            DataBindingUtil.inflate(inflater, R.layout.management_schedule_list_pop_up_binding, null, false)
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
        callScheduleApi(CommonUtility.getCurrentDate(context))
        observeScheduleViewModel(popupBinding,professorSubModuleDetailScreenViewModel)
        popupBinding.simpleCalendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth-${month + 1}-$year"
            val formatter = DateTimeFormatter.ofPattern("d-M-yyyy")
            val parsedDate = LocalDate.parse(selectedDate, formatter)
            val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            callScheduleApi(formattedDate)
            observeScheduleViewModel(popupBinding,professorSubModuleDetailScreenViewModel)
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
                if (managementViewScheduleAdapter != null) {
                    val searchText =
                        popupBinding.etSearch.text.toString().toLowerCase(
                            Locale.getDefault()
                        )
                    managementViewScheduleAdapter!!.filter(searchText)
                }
            }
        })
    }

    private fun observeScheduleViewModel(popupBinding: ManagementScheduleListPopUpBindingBinding,viewModel: ProfessorSubModuleDetailScreenViewModel) {
        viewModel.getScheduleApiValuesData()
            ?.observe(requireActivity(), Observer { viewScheduleData ->
                if (viewScheduleData != null) {
                    if (viewScheduleData.status == 403 && viewScheduleData.data.isNotEmpty()) {
                        showProfessorScheduleList(popupBinding,viewScheduleData,false)
                    } else {
                        showProfessorScheduleList(popupBinding,viewScheduleData,true)
                    }
                } else {
                    showProfessorScheduleList(popupBinding,viewScheduleData,false)
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showProfessorScheduleList(popupBinding: ManagementScheduleListPopUpBindingBinding,viewScheduleData: CommonResponseModel<ViewScheduleModel>?, hasValue: Boolean) {
        CommonUtility.cancelProgressDialog(activity)
        if (hasValue)
        {
            popupBinding.rlError.visibility = View.GONE
            scheduleList.clear()
            viewScheduleData?.let {
                val filteredItems = it.data.filter { schedule ->
                    schedule.status == "1" && schedule.is_deleted == "0"
                }
                scheduleList.addAll(filteredItems)
                managementViewScheduleAdapter =
                    ManagementViewScheduleAdapter(context, scheduleList)
                popupBinding.recyclerView.adapter = managementViewScheduleAdapter
                managementViewScheduleAdapter!!.notifyDataSetChanged()
            }
        }
        else
        {
            popupBinding.rlError.visibility = View.VISIBLE
        }
    }



}