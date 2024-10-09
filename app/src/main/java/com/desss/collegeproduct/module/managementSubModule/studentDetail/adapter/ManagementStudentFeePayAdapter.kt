package com.desss.collegeproduct.module.managementSubModule.studentDetail.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.AdapterManagementStudentFeePayBinding
import com.desss.collegeproduct.module.studentSubModule.feePay.model.FeePayModel


class ManagementStudentFeePayAdapter(private val context: Context?, private val feePayModelList: List<FeePayModel>) :
    RecyclerView.Adapter<ManagementStudentFeePayAdapter.ViewHolder>() {

    private var feePayModel: FeePayModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterManagementStudentFeePayBinding: AdapterManagementStudentFeePayBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_management_student_fee_pay,
            parent,
            false
        )
        return ViewHolder(adapterManagementStudentFeePayBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        feePayModel = feePayModelList[position]
        holder.binding.semesterContentTv.text = "Semester " + feePayModel!!.semester
        holder.binding.dateTv.text =feePayModel!!.date
        holder.binding.receiptValueTv.text = "#123456789"

        if (feePayModel!!.status.equals("1") || feePayModel!!.status == "1") {
            holder.binding.feesStatusValueTv.text = "Paid"
        } else {
            holder.binding.feesStatusValueTv.text = "Not Paid"
        }
    }

    override fun getItemCount(): Int {
        return feePayModelList.size
    }

    class ViewHolder(binding: AdapterManagementStudentFeePayBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterManagementStudentFeePayBinding

        init {
            this.binding = binding
        }
    }
}