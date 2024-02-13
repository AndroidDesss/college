package com.desss.collegeproduct.module.auth.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.CommonValidation
import com.desss.collegeproduct.databinding.ActivityChangePasswordScreenBinding
import com.desss.collegeproduct.module.auth.model.ChangePasswordModel
import com.desss.collegeproduct.module.auth.viewModel.ChangePasswordViewModel

class ChangePasswordScreen : AppCompatActivity() {

    private lateinit var changePasswordScreenBinding: ActivityChangePasswordScreenBinding

    private lateinit var changePasswordViewModel: ChangePasswordViewModel

    private var intentEmail: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changePasswordScreenBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_change_password_screen)
        getBundleData()
        initViewModel()
        initListener()
    }

    private fun getBundleData() {
        val intent = intent
        when {
            intent != null -> {
                intentEmail = intent.getStringExtra("email")!!
            }
        }
    }

    private fun initViewModel() {
        changePasswordViewModel = ChangePasswordViewModel(application, this)
    }

    private fun initListener() {
        changePasswordScreenBinding.btnContinue.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btnContinue -> {
                if (validateDetails()) {
                    CommonUtility.showProgressDialog(this)
                    changePasswordViewModel.callChangePassword(
                        this,
                        "forgot_new_passwrod",
                        intentEmail,
                        changePasswordScreenBinding.etConfirmPassword.text.toString()
                    )
                    observeViewModel(changePasswordViewModel)
                }
            }
        }
    }

    private fun observeViewModel(viewModel: ChangePasswordViewModel) {
        viewModel.getChangePasswordValues()?.observe(this,
            Observer { response ->
                handleChangePasswordData(response)
            })
    }

    private fun handleChangePasswordData(response: CommonResponseModel<ChangePasswordModel>) {
        if (response.status == 200) {
            CommonUtility.cancelProgressDialog(this)
            val otpDataList: List<ChangePasswordModel> = response.data
            val userOtp: ChangePasswordModel? = otpDataList.firstOrNull()
            userOtp?.let {
                if (it.msg == "Suceesfully Updated") {
                    CommonUtility.toastString("Successfully Updated..!", this)
                    CommonUtility.commonStartActivity(this, LoginScreen::class.java, null, false)
                }
            }
        }
    }

    private fun validateDetails(): Boolean {
        if (!CommonValidation.isEditTextNotEmpty(changePasswordScreenBinding.etPassword)) {
            changePasswordScreenBinding.etPassword.error = "Enter Password"
            return false
        } else if (!CommonValidation.isEditTextNotEmpty(changePasswordScreenBinding.etConfirmPassword)) {
            changePasswordScreenBinding.etConfirmPassword.error = "Enter Confirm Password"
            return false
        } else if (!changePasswordScreenBinding.etPassword.text.toString()
                .equals(changePasswordScreenBinding.etConfirmPassword.text.toString()) || changePasswordScreenBinding.etPassword.text.toString() != changePasswordScreenBinding.etConfirmPassword.text.toString()
        ) {
            changePasswordScreenBinding.etConfirmPassword.error = "Enter Same Password"
            changePasswordScreenBinding.etPassword.error = "Enter Same Password"
            return false
        } else {
            return true
        }
    }
}