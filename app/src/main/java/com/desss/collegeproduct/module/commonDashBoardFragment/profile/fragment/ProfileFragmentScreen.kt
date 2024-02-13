package com.desss.collegeproduct.module.commonDashBoardFragment.profile.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.FragmentProfileScreenBinding
import com.desss.collegeproduct.module.auth.activity.LoginScreen
import com.desss.collegeproduct.module.commonDashBoardFragment.profile.model.ProfileModel
import com.desss.collegeproduct.module.commonDashBoardFragment.profile.viewModel.ProfileFragmentScreenViewModel

class ProfileFragmentScreen : Fragment() {

    private lateinit var fragmentProfileFragmentScreenBinding: FragmentProfileScreenBinding

    private lateinit var fragmentProfileFragmentScreenViewModel: ProfileFragmentScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentProfileFragmentScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile_screen, container, false)
        initViewModel()
        initListener()
        handleVisibility()
        observeViewModel(fragmentProfileFragmentScreenViewModel)
        return fragmentProfileFragmentScreenBinding.root
    }

    private fun initListener() {
        fragmentProfileFragmentScreenBinding.logOut.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.logOut ->
                CommonUtility.showAlertDialog(context,
                    "",
                    "Are you sure you want to exit?",
                    "Yes",
                    "No",
                    object : CommonUtility.DialogClickListener {
                        override fun dialogOkBtnClicked(value: String?) {
                            CommonUtility.commonStartActivity(
                                activity!!,
                                LoginScreen::class.java,
                                null,
                                true
                            )
                        }

                        override fun dialogNoBtnClicked(value: String?) {}
                    }
                )
        }
    }

    private fun handleVisibility() {
        if (SharedPref.getRollId(context) == "4") //Student
        {
            fragmentProfileFragmentScreenBinding.joinDateTv.visibility = View.GONE
            fragmentProfileFragmentScreenBinding.joinDateValueTv.visibility = View.GONE
            fragmentProfileFragmentScreenBinding.professorUserIdTv.visibility = View.GONE
            fragmentProfileFragmentScreenBinding.professorUserIdValueTv.visibility = View.GONE
            fragmentProfileFragmentScreenBinding.detailsProfessorIdTv.visibility = View.GONE
            fragmentProfileFragmentScreenBinding.detailsProfessorIdValueTv.visibility = View.GONE
            fragmentProfileFragmentScreenBinding.salaryTv.visibility = View.GONE
            fragmentProfileFragmentScreenBinding.salaryValueTv.visibility = View.GONE
        } else if (SharedPref.getRollId(context) == "3") //Professor
        {
            fragmentProfileFragmentScreenBinding.tenthMarkSheetTv.visibility = View.GONE
            fragmentProfileFragmentScreenBinding.downloadTenthMarkSheet.visibility = View.GONE
            fragmentProfileFragmentScreenBinding.plusTwoMarkSheetTv.visibility = View.GONE
            fragmentProfileFragmentScreenBinding.downloadPlusTwoMarkSheet.visibility = View.GONE
            fragmentProfileFragmentScreenBinding.alternateMobileNumberTv.visibility = View.GONE
            fragmentProfileFragmentScreenBinding.alternateMobileNumberValueTv.visibility = View.GONE
        }
        callStudentApi()
    }

    private fun initViewModel() {
        fragmentProfileFragmentScreenViewModel =
            ProfileFragmentScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun callStudentApi() {
        CommonUtility.showProgressDialog(context)
        fragmentProfileFragmentScreenViewModel.callStudentProfileApi(
            requireActivity(),
            "read",
            "accounts_user",
            SharedPref.getId(context).toString()
        )
    }

    private fun observeViewModel(viewModel: ProfileFragmentScreenViewModel) {

        viewModel.getStudentProfileData()?.observe(requireActivity(), Observer { response ->
            handleStudentProfileData(response)
        })
    }

    @SuppressLint("SetTextI18n")
    private fun handleStudentProfileData(response: CommonResponseModel<ProfileModel>) {

        if (response.status == 200) {
            if (SharedPref.getRollId(context) == "4") {
                val profileList: List<ProfileModel> = response.data
                val userProfile: ProfileModel? = profileList.firstOrNull()
                userProfile?.let {
                    fragmentProfileFragmentScreenBinding.nameValueTv.text =
                        it.first_name + " " + it.last_name
                    fragmentProfileFragmentScreenBinding.emailPhoneValueTv.text = it.email

                    fragmentProfileFragmentScreenBinding.firstNameValueTv.text = it.first_name
                    fragmentProfileFragmentScreenBinding.lastNameValueTv.text = it.last_name
                    fragmentProfileFragmentScreenBinding.birthDateValueTv.text = it.dob
                    fragmentProfileFragmentScreenBinding.emailIdValueTv.text = it.email
                    fragmentProfileFragmentScreenBinding.mobileNumberValueTv.text = it.phone
                    fragmentProfileFragmentScreenBinding.alternateMobileNumberValueTv.text =
                        it.alter_phone
                    fragmentProfileFragmentScreenBinding.addressValueTv.text = it.address
                    CommonUtility.cancelProgressDialog(context)
                }
            } else if (SharedPref.getRollId(context) == "3") //Professor
            {
                val profileList: List<ProfileModel> = response.data
                val userProfile: ProfileModel? = profileList.firstOrNull()
                userProfile?.let {
                    fragmentProfileFragmentScreenBinding.nameValueTv.text =
                        it.first_name + " " + it.last_name
                    fragmentProfileFragmentScreenBinding.emailPhoneValueTv.text = it.email
                    fragmentProfileFragmentScreenBinding.firstNameValueTv.text = it.first_name
                    fragmentProfileFragmentScreenBinding.lastNameValueTv.text = it.last_name
                    fragmentProfileFragmentScreenBinding.joinDateValueTv.text = it.admission_date
                    fragmentProfileFragmentScreenBinding.birthDateValueTv.text = it.dob
                    fragmentProfileFragmentScreenBinding.professorUserIdValueTv.text = it.reg_no
                    fragmentProfileFragmentScreenBinding.detailsProfessorIdValueTv.text = it.id
                    fragmentProfileFragmentScreenBinding.birthDateValueTv.text = it.dob
                    fragmentProfileFragmentScreenBinding.emailIdValueTv.text = it.email
                    fragmentProfileFragmentScreenBinding.mobileNumberValueTv.text = it.phone
                    fragmentProfileFragmentScreenBinding.salaryValueTv.text = it.salary
                    fragmentProfileFragmentScreenBinding.addressValueTv.text = it.address
                    CommonUtility.cancelProgressDialog(context)
                }
            }
        }
    }

}