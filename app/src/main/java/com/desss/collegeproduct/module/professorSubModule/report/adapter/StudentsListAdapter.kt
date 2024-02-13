package com.desss.collegeproduct.module.professorSubModule.report.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.CommonValidation
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.AdapterStudentListBinding
import com.desss.collegeproduct.databinding.AddReportPopUpLayoutBinding
import com.desss.collegeproduct.module.professorSubModule.report.model.StudentListBasedModel
import com.desss.collegeproduct.module.professorSubModule.report.viewmodel.ReportFragmentScreenViewModel

class StudentsListAdapter(
    private val context: Context?,
    private var studentList: List<StudentListBasedModel>,
    private val reportFragmentScreenViewModel: ReportFragmentScreenViewModel
) : RecyclerView.Adapter<StudentsListAdapter.ViewHolder>() {

    private var studentModel: StudentListBasedModel? = null

    private var dataList: List<StudentListBasedModel> = studentList.toList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterStudentListBinding: AdapterStudentListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_student_list,
            parent,
            false
        )
        return ViewHolder(adapterStudentListBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        studentModel = studentList[position]
        holder.binding.studentNameTv.text =
            studentModel!!.first_name + " " + studentModel!!.last_name
        holder.binding.editIcon.setOnClickListener { v ->
            studentModel = studentList[position]
            showPopup(
                v,
                studentModel!!.first_name + "  " + studentModel!!.last_name,
                studentModel!!.reg_no,
                studentModel!!.id,
                holder
            )
        }
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    class ViewHolder(binding: AdapterStudentListBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterStudentListBinding

        init {
            this.binding = binding
        }
    }

    private fun showPopup(
        v: View,
        name: String,
        regNo: String,
        studentId: String,
        viewHolder: ViewHolder
    ) {
        val inflater = LayoutInflater.from(context)
        val popupBinding: AddReportPopUpLayoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.add_report_pop_up_layout, null, false)
        val popupWindow = PopupWindow(
            popupBinding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        )
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0)
        popupWindow.setFocusable(true)
        popupWindow.update()
        popupBinding.studentNameValueTv.text = name
        popupBinding.registerNoValueTv.text = regNo
        popupBinding.btnClose.setOnClickListener {
            popupWindow.dismiss()
        }
        popupBinding.updateButton.setOnClickListener {
            if (!CommonValidation.isEditTextNotEmpty(popupBinding.inputMessage)) {
                popupBinding.inputMessage.error = "Enter Reports"
            } else {
                postReports(popupBinding.inputMessage.text.toString(), name, regNo, studentId)
                observeViewModel(reportFragmentScreenViewModel, viewHolder, popupWindow)
            }
        }
    }

    private fun postReports(content: String, name: String, regNo: String, studentId: String) {
        CommonUtility.showProgressDialog(context)
        reportFragmentScreenViewModel.postReportsApi(
            context as Activity,
            "professor_add_reports",
            SharedPref.getId(context).toString(),
            studentId,
            content,
            regNo,
            name
        )
    }

    private fun observeViewModel(
        viewModel: ReportFragmentScreenViewModel,
        viewHolder: ViewHolder,
        popupWindow: PopupWindow
    ) {
        val lifecycleOwner = (viewHolder.itemView.context as? LifecycleOwner) ?: return

        viewModel.getPostReports()?.observe(lifecycleOwner, Observer { addReportsList ->
            if (addReportsList != null) {
                if (addReportsList.status == 400 && addReportsList.data.isNotEmpty()) {
                    CommonUtility.cancelProgressDialog(context)
                    popupWindow.dismiss()
                } else {
                    CommonUtility.toastString("Successfully Uploaded", context)
                    CommonUtility.cancelProgressDialog(context)
                    popupWindow.dismiss()
                }
            } else {
                CommonUtility.cancelProgressDialog(context)
                popupWindow.dismiss()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filter(query: String) {
        studentList = if (query.isEmpty()) {
            dataList
        } else {
            dataList.filter { it.first_name.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

}
