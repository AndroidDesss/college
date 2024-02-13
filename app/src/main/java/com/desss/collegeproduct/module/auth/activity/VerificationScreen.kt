package com.desss.collegeproduct.module.auth.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.CommonValidation
import com.desss.collegeproduct.databinding.ActivityVerificationScreenBinding
import com.desss.collegeproduct.module.auth.model.ForgotModel
import com.desss.collegeproduct.module.auth.viewModel.ForgotViewModel

class VerificationScreen : AppCompatActivity() {

    private lateinit var verificationScreenBinding: ActivityVerificationScreenBinding

    var intentOtp: String = ""

    var email: String = ""

    private lateinit var countDownTimer: CountDownTimer

    private lateinit var forgotViewModel: ForgotViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verificationScreenBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_verification_screen)

        countDownTimer = object : CountDownTimer(10000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                verificationScreenBinding.reSendContentTv.text =
                    "Retry in  " + secondsRemaining.toString() + " Seconds"
            }

            override fun onFinish() {
                verificationScreenBinding.reSendContentTv.visibility = View.INVISIBLE
                verificationScreenBinding.reSendTv.visibility = View.VISIBLE
            }
        }
        getBundleData()
        initViewModel()
        initListener()
    }

    @SuppressLint("SetTextI18n")
    private fun getBundleData() {
        val intent = intent
        when {
            intent != null -> {
                intentOtp = intent.getStringExtra("otp")!!
                email = intent.getStringExtra("email")!!
                verificationScreenBinding.forgotFirstContentTv.setText("Enter the 4-digit code sent to you at  $email")
                countDownTimer.start()
                Log.d("intentOtp", intentOtp)
                Log.d("intentOtp", email)
            }
        }
    }

    private fun initListener() {
        verificationScreenBinding.btnContinue.setOnClickListener(onClickListener)
        verificationScreenBinding.reSendTv.setOnClickListener(onClickListener)

        verificationScreenBinding.otp1.addTextChangedListener(object : TextWatcher {
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
                if (charSequence?.length == 1) {
                    verificationScreenBinding.otp2.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })

        verificationScreenBinding.otp2.addTextChangedListener(object : TextWatcher {
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
                if (charSequence?.length == 1) {
                    verificationScreenBinding.otp3.requestFocus()
                } else if (charSequence?.length == 0) {
                    verificationScreenBinding.otp1.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })

        verificationScreenBinding.otp3.addTextChangedListener(object : TextWatcher {
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
                if (charSequence?.length == 1) {
                    verificationScreenBinding.otp4.requestFocus()
                } else if (charSequence?.length == 0) {
                    verificationScreenBinding.otp2.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })

        verificationScreenBinding.otp4.addTextChangedListener(object : TextWatcher {
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
                if (charSequence?.length == 1) {
                    verificationScreenBinding.otp5.requestFocus()
                } else if (charSequence?.length == 0) {
                    verificationScreenBinding.otp3.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })

        verificationScreenBinding.otp5.addTextChangedListener(object : TextWatcher {
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
                if (charSequence?.length == 1) {
                    verificationScreenBinding.otp6.requestFocus()
                } else if (charSequence?.length == 0) {
                    verificationScreenBinding.otp4.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })

        verificationScreenBinding.otp6.addTextChangedListener(object : TextWatcher {
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
                if (charSequence?.length == 0) {
                    verificationScreenBinding.otp5.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })
    }

    private fun initViewModel() {
        forgotViewModel = ForgotViewModel(application, this)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btnContinue -> {
                if (validateDetails()) {
                    val otpNumber = verificationScreenBinding.otp1.getText()
                        .toString() + verificationScreenBinding.otp2.getText()
                        .toString() + verificationScreenBinding.otp3.getText()
                        .toString() + verificationScreenBinding.otp4.getText()
                        .toString() + verificationScreenBinding.otp5.getText()
                        .toString() + verificationScreenBinding.otp6.getText().toString();
                    if (otpNumber.equals(intentOtp) || otpNumber == intentOtp) {
                        val bundle = Bundle()
                        bundle.putString("email", email)
                        CommonUtility.commonStartActivity(
                            this,
                            ChangePasswordScreen::class.java,
                            bundle,
                            false
                        )
                    } else {
                        CommonUtility.toastString("Invalid OTP..!", this)
                    }
                } else {
                    CommonUtility.toastString("Enter Valid Otp..!", this)
                }
            }

            R.id.reSendTv -> {
                verificationScreenBinding.otp1.setText("")
                verificationScreenBinding.otp2.setText("")
                verificationScreenBinding.otp3.setText("")
                verificationScreenBinding.otp4.setText("")
                verificationScreenBinding.otp5.setText("")
                verificationScreenBinding.otp6.setText("")
                CommonUtility.showProgressDialog(this)
                forgotViewModel.callOtpApi(this, "open_email", email)
                observeViewModel(forgotViewModel)
            }
        }
    }

    private fun validateDetails(): Boolean {
        return if (!CommonValidation.isEditTextNotEmpty(verificationScreenBinding.otp1)
        ) false else if (!CommonValidation.isEditTextNotEmpty(verificationScreenBinding.otp2)
        ) false else if (!CommonValidation.isEditTextNotEmpty(verificationScreenBinding.otp3)
        ) false else if (!CommonValidation.isEditTextNotEmpty(verificationScreenBinding.otp4)
        ) false else if (!CommonValidation.isEditTextNotEmpty(verificationScreenBinding.otp5)
        ) false else if (!CommonValidation.isEditTextNotEmpty(verificationScreenBinding.otp6)) false else true
    }

    private fun observeViewModel(viewModel: ForgotViewModel) {
        viewModel.getOtpApi()?.observe(this,
            Observer { response ->
                handleForgotPasswordData(response)
            })
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
                    intentOtp = it.otp
                    verificationScreenBinding.reSendContentTv.visibility = View.VISIBLE
                    verificationScreenBinding.reSendTv.visibility = View.INVISIBLE
                    countDownTimer.start()
                }
            }
        }
    }

}