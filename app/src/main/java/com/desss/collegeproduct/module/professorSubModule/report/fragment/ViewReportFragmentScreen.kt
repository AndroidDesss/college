package com.desss.collegeproduct.module.professorSubModule.report.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.FragmentViewReportScreenBinding
import com.desss.collegeproduct.module.professorSubModule.report.adapter.ViewReportsAdapter
import com.desss.collegeproduct.module.professorSubModule.report.model.ViewReportsModel
import com.desss.collegeproduct.module.professorSubModule.report.viewmodel.ViewReportFragmentScreenViewModel


class ViewReportFragmentScreen : Fragment() {

    private lateinit var fragmentViewReportScreenBinding: FragmentViewReportScreenBinding

    private lateinit var viewReportFragmentScreenViewModel: ViewReportFragmentScreenViewModel

    private var viewReportsList = arrayListOf<ViewReportsModel>()

    private var viewReportsAdapter: ViewReportsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentViewReportScreenBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_view_report_screen,
            container,
            false
        )
        initViewModel()
        callApi()
        observeViewModel(viewReportFragmentScreenViewModel)
        return fragmentViewReportScreenBinding.root
    }

    private fun initViewModel() {
        viewReportFragmentScreenViewModel =
            ViewReportFragmentScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        viewReportFragmentScreenViewModel.callReportsValuesApi(
            requireActivity(), "read", "student_remarks",
            SharedPref.getId(context).toString(), "1"
        )
    }

    private fun observeViewModel(viewModel: ViewReportFragmentScreenViewModel) {
        viewModel.getReportsListData()?.observe(requireActivity(), Observer { reportsList ->
            if (reportsList != null) {
                if (reportsList.status == 400 && reportsList.data.isNotEmpty()) {
                    CommonUtility.cancelProgressDialog(activity)
                    fragmentViewReportScreenBinding.rlError.visibility = View.VISIBLE
                } else {
                    fragmentViewReportScreenBinding.rlError.visibility = View.GONE
                    handleReportsListData(reportsList)
                }
            } else {
                CommonUtility.cancelProgressDialog(activity)
                fragmentViewReportScreenBinding.rlError.visibility = View.VISIBLE
            }
        })
    }

    private fun handleReportsListData(reportsData: CommonResponseModel<ViewReportsModel>?) {
        viewReportsList.clear()
        reportsData?.let {
            val filteredItems = it.data.filter { reports ->
                reports.status == "1" && reports.is_deleted == "0"
            }
            viewReportsList.addAll(filteredItems)
            setBindingAdapter(viewReportsList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setBindingAdapter(viewReportsModel: List<ViewReportsModel>) {
        viewReportsAdapter = ViewReportsAdapter(context, viewReportsModel)
        fragmentViewReportScreenBinding.recyclerView.adapter = viewReportsAdapter
        viewReportsAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(context)
    }
}