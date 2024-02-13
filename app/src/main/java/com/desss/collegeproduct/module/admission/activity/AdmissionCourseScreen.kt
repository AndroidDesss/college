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
import com.desss.collegeproduct.databinding.ActivityAdmissionCourseScreenBinding
import com.desss.collegeproduct.module.admission.adapter.AdmissionCourseAdapter
import com.desss.collegeproduct.module.admission.adapter.AdmissionDegreeAdapter
import com.desss.collegeproduct.module.admission.model.CourseModel
import com.desss.collegeproduct.module.admission.model.DegreeModel
import com.desss.collegeproduct.module.admission.viewModel.AdmissionViewModel

class AdmissionCourseScreen : AppCompatActivity() {

    private lateinit var admissionViewModel: AdmissionViewModel

    private lateinit var activityAdmissionCourseScreenBinding: ActivityAdmissionCourseScreenBinding

    private lateinit var degreeId: String

    private var admissionCourseAdapter: AdmissionCourseAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAdmissionCourseScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_admission_course_screen)
        initViewModel()
        getBundleData()
        observeViewModel(admissionViewModel)
    }

    private fun getBundleData() {
        degreeId = intent.getStringExtra("degreeId").toString()
        callApi()
    }

    private fun initViewModel() {
        admissionViewModel = AdmissionViewModel(application, this)
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(this)
        admissionViewModel.callCourseApi(
            this,
            "ams",
            degreeId
        )
    }

    private fun observeViewModel(viewModel: AdmissionViewModel) {
        viewModel.getCourseDataObservable()?.observe(this, Observer { courseData ->
            if (courseData != null) {
                if (courseData.status == 403 && courseData.data.isNotEmpty()) {
                    CommonUtility.cancelProgressDialog(this)
                    activityAdmissionCourseScreenBinding.rlError.visibility = View.VISIBLE
                } else {
                    activityAdmissionCourseScreenBinding.rlError.visibility = View.GONE
                    handleCourseData(courseData)
                }
            } else {
                CommonUtility.cancelProgressDialog(this)
                activityAdmissionCourseScreenBinding.rlError.visibility = View.VISIBLE
            }
        })
    }

    private fun handleCourseData(degreeData: CommonResponseModel<CourseModel>?) {
        val courseDataList: List<CourseModel> = degreeData!!.data
        setBindingAdapter(courseDataList)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun setBindingAdapter(courseModel: List<CourseModel>) {
        admissionCourseAdapter = AdmissionCourseAdapter(this, courseModel)
        activityAdmissionCourseScreenBinding.recyclerView.adapter = admissionCourseAdapter
        admissionCourseAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(this)
    }
}