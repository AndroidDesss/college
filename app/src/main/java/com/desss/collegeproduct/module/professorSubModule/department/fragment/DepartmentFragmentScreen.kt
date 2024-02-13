package com.desss.collegeproduct.module.professorSubModule.department.fragment

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
import com.desss.collegeproduct.databinding.FragmentDepartmentScreenBinding
import com.desss.collegeproduct.module.professorSubModule.department.adapter.DepartmentAdapter
import com.desss.collegeproduct.module.professorSubModule.department.model.DepartmentModel
import com.desss.collegeproduct.module.professorSubModule.department.viewmodel.DepartmentFragmentScreenViewModel

class DepartmentFragmentScreen : Fragment() {

    private lateinit var fragmentDepartmentScreenBinding: FragmentDepartmentScreenBinding

    private lateinit var departmentFragmentScreenViewModel: DepartmentFragmentScreenViewModel

    private var departmentList = arrayListOf<DepartmentModel>()

    private var departmentAdapter: DepartmentAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentDepartmentScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_department_screen, container, false)
        initViewModel()
        callApi()
        observeViewModel(departmentFragmentScreenViewModel)
        return fragmentDepartmentScreenBinding.root
    }

    private fun initViewModel() {
        departmentFragmentScreenViewModel =
            DepartmentFragmentScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        departmentFragmentScreenViewModel.callDepartmentApi(
            requireActivity(),
            "read",
            "professor_departments",
            SharedPref.getId(context).toString()
        )
    }

    private fun observeViewModel(viewModel: DepartmentFragmentScreenViewModel) {
        viewModel.getAttendanceData()?.observe(requireActivity(), Observer { departmentData ->
            if (departmentData != null) {
                if (departmentData.status == 403 && departmentData.data.isNotEmpty()) {
                    CommonUtility.cancelProgressDialog(activity)
                    fragmentDepartmentScreenBinding.rlError.visibility = View.VISIBLE
                } else {
                    fragmentDepartmentScreenBinding.rlError.visibility = View.GONE
                    handleDepartmentData(departmentData)
                }
            } else {
                CommonUtility.cancelProgressDialog(activity)
                fragmentDepartmentScreenBinding.rlError.visibility = View.VISIBLE
            }
        })
    }

    private fun handleDepartmentData(departmentData: CommonResponseModel<DepartmentModel>?) {
        departmentList.clear()
        departmentData?.let {
            val filteredItems = it.data.filter { syllabus ->
                syllabus.is_status == "1" && syllabus.is_deleted == "0"
            }
            departmentList.addAll(filteredItems)
            setBindingAdapter(departmentList)
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun setBindingAdapter(departmentModel: List<DepartmentModel>) {
        departmentAdapter = DepartmentAdapter(context, departmentModel)
        fragmentDepartmentScreenBinding.recyclerView.adapter = departmentAdapter
        departmentAdapter!!.notifyDataSetChanged()
        fragmentDepartmentScreenBinding.professorNameTv.text =
            "Prof." + SharedPref.getFirstName(context) + " " + SharedPref.getLastName(context)
        CommonUtility.cancelProgressDialog(context)
    }

}