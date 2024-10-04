package com.desss.collegeproduct.module.managementSubModule.studentDetail.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.FragmentStudentSubModuleDetailScreenBinding
import com.desss.collegeproduct.databinding.ManagementStudentDetailsListPopUpLayoutBinding
import com.desss.collegeproduct.databinding.ManagementStudentProfilePopUpLayoutBinding
import com.desss.collegeproduct.databinding.ShowStudentsListPopupBinding
import com.desss.collegeproduct.module.commonDashBoardFragment.profile.model.ProfileModel
import com.desss.collegeproduct.module.managementSubModule.studentDetail.adapter.ManagementStudentFeePayAdapter
import com.desss.collegeproduct.module.managementSubModule.studentDetail.model.StudentListManagementBasedModel
import com.desss.collegeproduct.module.managementSubModule.studentDetail.viewmodel.StudentSubModuleDetailScreenViewModel
import com.desss.collegeproduct.module.professorSubModule.professorAttendance.adapter.ProfessorNameListAdapter
import com.desss.collegeproduct.module.studentSubModule.attendance.adapter.StudentAttendanceAdapter
import com.desss.collegeproduct.module.studentSubModule.attendance.model.StudentAttendanceModel
import com.desss.collegeproduct.module.studentSubModule.feePay.adapter.FeePayAdapter
import com.desss.collegeproduct.module.studentSubModule.feePay.model.FeePayModel
import com.desss.collegeproduct.module.studentSubModule.remarks.adapter.RemarksAdapter
import com.desss.collegeproduct.module.studentSubModule.remarks.model.RemarksModel

class StudentSubModuleDetailFragmentScreen : Fragment() {

    private lateinit var fragmentStudentSubModuleDetailFragmentScreen: FragmentStudentSubModuleDetailScreenBinding

    private lateinit var studentSubModuleDetailScreenViewModel: StudentSubModuleDetailScreenViewModel

    private lateinit var commonStudentId: String

    private var reportsList = arrayListOf<RemarksModel>()

    private var reportsAdapter: RemarksAdapter? = null

    private var studentAttendanceAdapter: StudentAttendanceAdapter? = null

    private var managementStudentFeePayAdapter: ManagementStudentFeePayAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentStudentSubModuleDetailFragmentScreen =
            DataBindingUtil.inflate(inflater, R.layout.fragment_student_sub_module_detail_screen, container, false)
        getBundleData()
        initViewModel()
        initListener()
        return fragmentStudentSubModuleDetailFragmentScreen.root
    }

    fun getBundleData() {
        val bundle = arguments
        if (bundle != null) {
            commonStudentId = bundle.getString("studentId").toString()
        }
    }

    private fun initViewModel() {
        studentSubModuleDetailScreenViewModel =
            StudentSubModuleDetailScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun initListener() {
        fragmentStudentSubModuleDetailFragmentScreen.profileDetailCardView.setOnClickListener(onClickListener)
        fragmentStudentSubModuleDetailFragmentScreen.feeDetailCardView.setOnClickListener(onClickListener)
        fragmentStudentSubModuleDetailFragmentScreen.reportDetailCardView.setOnClickListener(onClickListener)
        fragmentStudentSubModuleDetailFragmentScreen.attendanceDetailCardView.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.profileDetailCardView -> {
                callStudentProfileDetailApi()
                observeViewModel(view,studentSubModuleDetailScreenViewModel, 1)
            }
            R.id.feeDetailCardView -> {
                callStudentFeeDetailApi()
                observeViewModel(view,studentSubModuleDetailScreenViewModel, 2)
            }
            R.id.reportDetailCardView -> {
                callStudentReportDetailApi()
                observeViewModel(view,studentSubModuleDetailScreenViewModel, 3)
            }
            R.id.attendanceDetailCardView -> {
                callStudentAttendanceDetailApi()
                observeViewModel(view,studentSubModuleDetailScreenViewModel, 4)
            }
        }
    }

    private fun callStudentProfileDetailApi() {
        CommonUtility.showProgressDialog(context)
        studentSubModuleDetailScreenViewModel.callStudentProfileApi(
            requireActivity(),
            "read",
            "accounts_user",
            commonStudentId
        )
    }

    private fun callStudentFeeDetailApi() {
        CommonUtility.showProgressDialog(context)
        studentSubModuleDetailScreenViewModel.callFeePayApi(
            requireActivity(), "read", "student_fees",
            commonStudentId
        )
    }

    private fun callStudentReportDetailApi() {
        CommonUtility.showProgressDialog(context)
        studentSubModuleDetailScreenViewModel.callRemarksApi(
            requireActivity(),
            "read",
            "student_remarks",
            commonStudentId
        )
    }

    private fun callStudentAttendanceDetailApi() {
        CommonUtility.showProgressDialog(context)
        studentSubModuleDetailScreenViewModel.callAttendanceApi(
            requireActivity(),
            "student_attendence",
            commonStudentId
        )
    }

    private fun observeViewModel(view: View,viewModel: StudentSubModuleDetailScreenViewModel,position: Int) {
        if (position == 1)
        {
            viewModel.getStudentProfileData()?.observe(requireActivity(), Observer { profileData ->
                showStudentProfileList(view,profileData)
            })
        }
        else if(position == 2)
        {
            viewModel.getFeePayData()?.observe(requireActivity(), Observer { feePayData ->
                if (feePayData != null) {
                    if (feePayData.status == 403 && feePayData.data.isNotEmpty()) {
                        showStudentFeesDetailList(view,"Fee Details",feePayData,false)
                    } else {
                        showStudentFeesDetailList(view,"Fee Details",feePayData,true)
                    }
                } else {
                    showStudentFeesDetailList(view,"Fee Details",feePayData,false)
                }
            })
        }
        else if(position == 3)
        {
            viewModel.getRemarksData()?.observe(requireActivity(), Observer { remarksData ->
                if (remarksData != null) {
                    if (remarksData.status == 403 && remarksData.data.isNotEmpty()) {
                        showStudentReportList(view,"Reports",remarksData,false)
                    } else {
                        showStudentReportList(view,"Reports",remarksData,true)
                    }
                } else {
                    showStudentReportList(view,"Reports",remarksData,false)
                }
            })
        }
        else if(position == 4)
        {
            viewModel.getAttendanceData()?.observe(requireActivity(),
                Observer { attendanceData ->
                    if (attendanceData != null) {
                        if (attendanceData.status == 403 && attendanceData.data.isNotEmpty()) {
                            showStudentAttendanceList(view,"Attendance",attendanceData,false)
                        } else {
                            showStudentAttendanceList(view,"Attendance",attendanceData,true)
                        }
                    } else {
                        showStudentAttendanceList(view,"Attendance",attendanceData,false)
                    }
                })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showStudentReportList(v: View, screenName: String, reportsData: CommonResponseModel<RemarksModel>?,hasValue: Boolean) {
        CommonUtility.cancelProgressDialog(activity)
        val inflater = LayoutInflater.from(context)
        val popupBinding: ManagementStudentDetailsListPopUpLayoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.management_student_details_list_pop_up_layout, null, false)
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
        if (hasValue)
        {
            popupBinding.rlError.visibility = View.GONE
            reportsList.clear()
            reportsData?.let {
                val filteredItems = it.data.filter { remarks ->
                    remarks.status == "1" && remarks.is_deleted == "0"
                }
                reportsList.addAll(filteredItems)
                reportsAdapter = RemarksAdapter(context, reportsList)
                popupBinding.recyclerView.adapter = reportsAdapter
                reportsAdapter!!.notifyDataSetChanged()
            }
        }
        else
        {
            popupBinding.rlError.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showStudentAttendanceList(v: View, screenName: String, attendanceData: CommonResponseModel<StudentAttendanceModel>?, hasValue: Boolean) {
        CommonUtility.cancelProgressDialog(activity)
        val inflater = LayoutInflater.from(context)
        val popupBinding: ManagementStudentDetailsListPopUpLayoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.management_student_details_list_pop_up_layout, null, false)
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
        if (hasValue)
        {
            popupBinding.rlError.visibility = View.GONE
            val attendanceDataList: List<StudentAttendanceModel> = attendanceData!!.data
            studentAttendanceAdapter = StudentAttendanceAdapter(context, attendanceDataList)
            popupBinding.recyclerView.adapter = studentAttendanceAdapter
            studentAttendanceAdapter!!.notifyDataSetChanged()
        }
        else
        {
            popupBinding.rlError.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun showStudentProfileList(v: View, profileData: CommonResponseModel<ProfileModel>?) {
        CommonUtility.cancelProgressDialog(activity)
        val inflater = LayoutInflater.from(context)
        val popupBinding: ManagementStudentProfilePopUpLayoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.management_student_profile_pop_up_layout, null, false)
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
                popupBinding.birthDateValueTv.text = it.dob
                popupBinding.emailIdValueTv.text = it.email
                popupBinding.mobileNumberValueTv.text = it.phone
                popupBinding.alternateMobileNumberValueTv.text = it.alter_phone
                popupBinding.addressValueTv.text = it.address
                CommonUtility.cancelProgressDialog(context)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showStudentFeesDetailList(v: View, screenName: String, feePayData: CommonResponseModel<FeePayModel>?, hasValue: Boolean) {
        CommonUtility.cancelProgressDialog(activity)
        val inflater = LayoutInflater.from(context)
        val popupBinding: ManagementStudentDetailsListPopUpLayoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.management_student_details_list_pop_up_layout, null, false)
        val popupWindow = PopupWindow(
            popupBinding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        )
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0)
        popupWindow.isFocusable = true
        popupWindow.update()
        popupBinding.btnClose.setOnClickListener {
            popupWindow.dismiss()
        }
        popupBinding.screenNameTv.text = screenName
        if (hasValue)
        {
            popupBinding.rlError.visibility = View.GONE
            val feePayList: List<FeePayModel> = feePayData!!.data
            managementStudentFeePayAdapter = ManagementStudentFeePayAdapter(context, feePayList)
            popupBinding.recyclerView.adapter = managementStudentFeePayAdapter
            managementStudentFeePayAdapter!!.notifyDataSetChanged()
        }
        else
        {
            popupBinding.rlError.visibility = View.VISIBLE
        }
    }
}