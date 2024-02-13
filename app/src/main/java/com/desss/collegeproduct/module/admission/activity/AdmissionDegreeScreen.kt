package com.desss.collegeproduct.module.admission.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.databinding.ActivityAdmissionDegreeBinding
import com.desss.collegeproduct.module.admission.adapter.AdmissionDegreeAdapter
import com.desss.collegeproduct.module.admission.model.DegreeModel
import com.desss.collegeproduct.module.admission.viewModel.AdmissionViewModel

class AdmissionDegreeScreen : AppCompatActivity() {

    private lateinit var admissionDegreeBinding: ActivityAdmissionDegreeBinding

    private lateinit var admissionViewModel: AdmissionViewModel

    private var admissionDegreeAdapter: AdmissionDegreeAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        admissionDegreeBinding = DataBindingUtil.setContentView(this, R.layout.activity_admission_degree)
        initViewModel()
        callApi()
        observeViewModel(admissionViewModel)
    }

    private fun initViewModel() {
        admissionViewModel = AdmissionViewModel(application, this)
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(this)
        admissionViewModel.callDegreeApi(
            this,
            "read",
            "degrees",
            "1",
            "0"
        )
    }

    private fun observeViewModel(viewModel: AdmissionViewModel) {
        viewModel.getDegreeDataObservable()?.observe(this, Observer { degreeData ->
            if (degreeData != null) {
                if (degreeData.status == 403 && degreeData.data.isNotEmpty()) {
                    CommonUtility.cancelProgressDialog(this)
                    admissionDegreeBinding.rlError.visibility = View.VISIBLE
                } else {
                    admissionDegreeBinding.rlError.visibility = View.GONE
                    handleDegreesData(degreeData)
                }
            } else {
                CommonUtility.cancelProgressDialog(this)
                admissionDegreeBinding.rlError.visibility = View.VISIBLE
            }
        })
    }

    private fun handleDegreesData(degreeData: CommonResponseModel<DegreeModel>?) {
        val degreeDataList: List<DegreeModel> = degreeData!!.data
        setBindingAdapter(degreeDataList)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun setBindingAdapter(degreeModel: List<DegreeModel>) {
        admissionDegreeAdapter = AdmissionDegreeAdapter(this, degreeModel)
        admissionDegreeBinding.recyclerView.adapter = admissionDegreeAdapter
        admissionDegreeAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(this)
    }

}