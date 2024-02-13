package com.desss.collegeproduct.module.admission.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.CommonValidation
import com.desss.collegeproduct.databinding.ActivityAdmissionFormScreenBinding
import com.desss.collegeproduct.module.admission.model.AdmissionModel
import com.desss.collegeproduct.module.admission.viewModel.AdmissionViewModel
import com.desss.collegeproduct.module.auth.activity.LoginScreen

class AdmissionFormScreen : AppCompatActivity() {

    private lateinit var admissionViewModel: AdmissionViewModel

    private lateinit var activityAdmissionFormScreenBinding: ActivityAdmissionFormScreenBinding

    private lateinit var amsId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAdmissionFormScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_admission_form_screen)
        initViewModel()
        getBundleData()
        initListener()
    }

    private fun getBundleData() {
        amsId = intent.getStringExtra("amsId").toString()
    }

    private fun initViewModel() {
        admissionViewModel = AdmissionViewModel(application, this)
    }

    private fun initListener() {
        activityAdmissionFormScreenBinding.btnSubmit.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btnSubmit -> if (validateDetails()) {
                CommonUtility.showProgressDialog(this)
                admissionViewModel.submitAmsDetails(
                    this,
                    "ams_details",
                    amsId,
                    activityAdmissionFormScreenBinding.etFirstName.text.toString(),
                    activityAdmissionFormScreenBinding.etLastName.text.toString(),
                    activityAdmissionFormScreenBinding.etEmail.text.toString(),
                    activityAdmissionFormScreenBinding.etPhoneNumber.text.toString(),
                    activityAdmissionFormScreenBinding.etAlterPhoneNumber.text.toString()
                )
                observeViewModel(admissionViewModel)
            }
        }
    }

    private fun observeViewModel(viewModel: AdmissionViewModel) {
        viewModel.getAmsDataObservable()?.observe(this,
            Observer { response ->
                if (response != null) {
                    if (response.status == 403 && response.data.isNotEmpty()) {
                        CommonUtility.toastString("Something went wrong..!",this)
                    } else {
                        handleAmsData(response)
                    }
                } else {
                    CommonUtility.cancelProgressDialog(this)
                    CommonUtility.toastString("Something went wrong..!",this)
                }
            })
    }

    private fun handleAmsData(response: CommonResponseModel<AdmissionModel>) {
        if (response.status == 200) {
            CommonUtility.cancelProgressDialog(this)
            val loginDataList: List<AdmissionModel> = response.data
            val userProfile: AdmissionModel? = loginDataList.firstOrNull()
            userProfile?.let {
                if (it.msg == "Inserted Successfully") {
                    startIntent()
                }
            }
        }
    }

    private fun startIntent() {
        CommonUtility.commonStartActivity(this, LoginScreen::class.java, null, true)
    }

    private fun validateDetails(): Boolean {
        if (!CommonValidation.isEditTextNotEmpty(activityAdmissionFormScreenBinding.etFirstName)) {
            activityAdmissionFormScreenBinding.etFirstName.error = "Enter First Name"
            return false
        } else if (!CommonValidation.isEditTextNotEmpty(activityAdmissionFormScreenBinding.etLastName)) {
            activityAdmissionFormScreenBinding.etLastName.error = "Enter Last Name"
            return false
        } else if (!CommonValidation.isEditTextNotEmpty(activityAdmissionFormScreenBinding.etEmail)) {
            activityAdmissionFormScreenBinding.etEmail.error = "Enter Email Id"
            return false
        } else if (!CommonValidation.isValidEmail(activityAdmissionFormScreenBinding.etEmail.text.toString())) {
            activityAdmissionFormScreenBinding.etEmail.error = "Enter Valid Email Id"
            return false
        } else if (!CommonValidation.isEditTextNotEmpty(activityAdmissionFormScreenBinding.etPhoneNumber)) {
            activityAdmissionFormScreenBinding.etPhoneNumber.error = "Enter Phone Number"
            return false
        } else if (activityAdmissionFormScreenBinding.etPhoneNumber.text!!.length < 10) {
            activityAdmissionFormScreenBinding.etPhoneNumber.error = "Enter Valid Phone Number"
            return false
        } else if (!CommonValidation.isEditTextNotEmpty(activityAdmissionFormScreenBinding.etAlterPhoneNumber)) {
            activityAdmissionFormScreenBinding.etAlterPhoneNumber.error = "Enter Phone Number"
            return false
        } else if (activityAdmissionFormScreenBinding.etAlterPhoneNumber.text!!.length < 10) {
            activityAdmissionFormScreenBinding.etAlterPhoneNumber.error = "Enter Valid Phone Number"
            return false
        }else {
            return true
        }
    }
}