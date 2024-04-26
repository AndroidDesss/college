package com.desss.collegeproduct.module.auth.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.CommonValidation
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.ActivityLoginScreenBinding
import com.desss.collegeproduct.module.admission.activity.AdmissionDegreeScreen
import com.desss.collegeproduct.module.auth.model.LoginModel
import com.desss.collegeproduct.module.auth.viewModel.LoginViewModel
import com.desss.collegeproduct.module.dashboard.DashBoardScreen

class LoginScreen : AppCompatActivity() {

    private lateinit var loginScreenBinding: ActivityLoginScreenBinding

    private lateinit var loginScreenViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_login_screen)
        initViewModel()
        initListener()
    }

    private fun initViewModel() {
        loginScreenViewModel = LoginViewModel(application, this)
    }

    private fun initListener() {
        loginScreenBinding.btnLogin.setOnClickListener(onClickListener)
        loginScreenBinding.forgotPasswordTv.setOnClickListener(onClickListener)
        loginScreenBinding.admissionTv.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btnLogin -> if (validateDetails()) {
                CommonUtility.showProgressDialog(this)
                loginScreenViewModel.submitLogin(
                    this,
                    "login",
                    loginScreenBinding.etUserId.text.toString(),
                    loginScreenBinding.etPassword.text.toString()
                )
                observeViewModel(loginScreenViewModel)
            }

            R.id.forgotPasswordTv -> {
                CommonUtility.commonStartActivity(
                    this,
                    ForgotPasswordScreen::class.java,
                    null,
                    false
                )
            }
            R.id.admissionTv -> {
                CommonUtility.commonStartActivity(
                    this,
                    AdmissionDegreeScreen::class.java,
                    null,
                    false
                )
            }
        }
    }

    private fun validateDetails(): Boolean {
        if (!CommonValidation.isEditTextNotEmpty(loginScreenBinding.etUserId)) {
            loginScreenBinding.etUserId.error = "Enter Email Id"
            return false
        } else if (!CommonValidation.isEditTextNotEmpty(loginScreenBinding.etPassword)) {
            loginScreenBinding.etPassword.error = "Enter Password"
            return false
        } else {
            return true
        }
    }

    private fun observeViewModel(viewModel: LoginViewModel) {
        viewModel.submitLoginObservable()?.observe(this,
            Observer { response ->
                if (response != null) {
                    if (response.status == 403 && response.data.isNotEmpty()) {
                        CommonUtility.toastString("Invalid Credentials",this)
                    } else {
                        handleLoginData(response)
                    }
                } else {
                    CommonUtility.cancelProgressDialog(this)
                    CommonUtility.toastString("Invalid Credentials",this)
                }
            })
    }

    private fun startIntent() {
        CommonUtility.commonStartActivity(this, DashBoardScreen::class.java, null, false)
    }

    private fun handleLoginData(response: CommonResponseModel<LoginModel>) {
        if (response.status == 200) {
            CommonUtility.cancelProgressDialog(this)
            val loginDataList: List<LoginModel> = response.data
            val userProfile: LoginModel? = loginDataList.firstOrNull()
            userProfile?.let {
                if (it.is_status == "1" && it.is_deleted == "0") {
                    SharedPref.setFirstName(this, it.first_name)
                    SharedPref.setLastName(this, it.last_name)
                    SharedPref.setEmailId(this, it.email)
                    SharedPref.setId(this, it.id)
                    SharedPref.setRegisterNo(this, it.reg_no)
                    SharedPref.setRollId(this, it.roll_id)
                    SharedPref.setDegree(this, it.degree)
                    SharedPref.setCourse(this, it.cource)
                    SharedPref.setSemester(this, it.semester)
                    SharedPref.setSection(this, it.section)
                    SharedPref.setParentLogin(this, response.parent)

                    startIntent()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        CommonUtility.showAlertDialog(this, "", "Are you sure you want to exit?", "Yes", "No",
            object : CommonUtility.DialogClickListener {
                override fun dialogOkBtnClicked(value: String?) {
                    finishAffinity()
                }
                override fun dialogNoBtnClicked(value: String?) {}
            }
        )
    }
}