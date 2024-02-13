package com.desss.collegeproduct.module.auth.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.CommonValidation
import com.desss.collegeproduct.databinding.ActivityForgotPasswordScreenBinding
import com.desss.collegeproduct.module.auth.model.ForgotModel
import com.desss.collegeproduct.module.auth.viewModel.ForgotViewModel

class ForgotPasswordScreen : AppCompatActivity() {

    private lateinit var forgotPasswordScreenBinding: ActivityForgotPasswordScreenBinding

    private lateinit var forgotViewModel: ForgotViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotPasswordScreenBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_forgot_password_screen)
        initViewModel()
        initListener()
    }

    private fun initViewModel() {
        forgotViewModel = ForgotViewModel(application, this)
    }

    private fun initListener() {
        forgotPasswordScreenBinding.btnContinue.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btnContinue -> if (validateDetails()) {
                CommonUtility.showProgressDialog(this)
                forgotViewModel.callOtpApi(
                    this,
                    "open_email",
                    forgotPasswordScreenBinding.etEmailPhone.text.toString()
                )
                observeViewModel(forgotViewModel)
            }
        }
    }

    private fun observeViewModel(viewModel: ForgotViewModel) {
        viewModel.getOtpApi()?.observe(this,
            Observer { response ->
                if (response != null) {
                    handleForgotPasswordData(response)
                } else {
                    CommonUtility.cancelProgressDialog(this)
                    CommonUtility.toastString("Please register..!", this)
                }
            })
    }

    private fun validateDetails(): Boolean {
        return if (!CommonValidation.isEditTextNotEmpty(forgotPasswordScreenBinding.etEmailPhone)) {
            forgotPasswordScreenBinding.etEmailPhone.error = "Enter Email Id"
            false
        } else {
            true
        }
    }

    private fun handleForgotPasswordData(response: CommonResponseModel<ForgotModel>) {
        if (response.status == 200) {
            CommonUtility.cancelProgressDialog(this)
            val otpDataList: List<ForgotModel> = response.data
            val userOtp: ForgotModel? = otpDataList.firstOrNull()
            userOtp?.let {
                if (it.errmsg == "No email found") {
                    CommonUtility.toastString("Please register..!", this)
                } else {
                    val bundle = Bundle()
                    bundle.putString("otp", it.otp)
                    bundle.putString(
                        "email",
                        forgotPasswordScreenBinding.etEmailPhone.text.toString()
                    )
                    CommonUtility.commonStartActivity(
                        this,
                        VerificationScreen::class.java,
                        bundle,
                        false
                    )
                }
            }
        }
    }
}