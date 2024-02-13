package com.desss.collegeproduct.module.managementSubModule.professorDetail.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.databinding.FragmentProfessorDetailScreenBinding
import com.desss.collegeproduct.module.managementSubModule.professorDetail.adapter.TotalProfessorAdapter
import com.desss.collegeproduct.module.managementSubModule.professorDetail.model.TotalProfessorModel
import com.desss.collegeproduct.module.managementSubModule.professorDetail.viewmodel.ProfessorModuleDetailScreenViewModel
import java.util.Locale

class ProfessorDetailFragmentScreen : Fragment() {

    private lateinit var fragmentProfessorDetailScreenBinding: FragmentProfessorDetailScreenBinding

    private lateinit var professorModuleDetailScreenViewModel: ProfessorModuleDetailScreenViewModel

    private var professorTotalList = arrayListOf<TotalProfessorModel>()

    private var professorTotalAdapter: TotalProfessorAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProfessorDetailScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_professor_detail_screen, container, false)
        initViewModel()
        callProfessorListApi()
        initListener()
        observeViewModel(professorModuleDetailScreenViewModel)
        return fragmentProfessorDetailScreenBinding.root
    }

    private fun initViewModel() {
        professorModuleDetailScreenViewModel =
            ProfessorModuleDetailScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun callProfessorListApi() {
        CommonUtility.showProgressDialog(context)
        professorModuleDetailScreenViewModel.callProfessorListApi(
            requireActivity(),
            "read",
            "accounts_user",
            "3"
        )
    }

    private fun observeViewModel(viewModel: ProfessorModuleDetailScreenViewModel) {
        viewModel.getProfessorListData()?.observe(requireActivity(), Observer { professorListData ->
            if (professorListData != null) {
                if (professorListData.status == 403 && professorListData.data.isNotEmpty()) {
                    CommonUtility.cancelProgressDialog(activity)
                    fragmentProfessorDetailScreenBinding.rlError.visibility = View.VISIBLE
                } else {
                    fragmentProfessorDetailScreenBinding.rlError.visibility = View.GONE
                    handleProfessorListData(professorListData)
                }
            } else {
                CommonUtility.cancelProgressDialog(activity)
                fragmentProfessorDetailScreenBinding.rlError.visibility = View.VISIBLE
            }
        })
    }
    private fun handleProfessorListData(departmentData: CommonResponseModel<TotalProfessorModel>?) {
        professorTotalList.clear()
        departmentData?.let {
            val filteredItems = it.data.filter { syllabus ->
                syllabus.is_status == "1" && syllabus.is_deleted == "0"
            }
            professorTotalList.addAll(filteredItems)
            setBindingAdapter(professorTotalList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setBindingAdapter(totalProfessorModel: List<TotalProfessorModel>) {
        professorTotalAdapter = TotalProfessorAdapter(context, totalProfessorModel)
        fragmentProfessorDetailScreenBinding.recyclerView.adapter = professorTotalAdapter
        professorTotalAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(context)
    }

    private fun initListener() {
        fragmentProfessorDetailScreenBinding.etSearch.addTextChangedListener(object : TextWatcher {
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
            }
            override fun afterTextChanged(editable: Editable?) {
                if (professorTotalAdapter != null) {
                    val searchText = fragmentProfessorDetailScreenBinding.etSearch.text.toString()
                        .toLowerCase(Locale.getDefault())
                    professorTotalAdapter!!.filter(searchText)
                }
            }
        })
    }

}