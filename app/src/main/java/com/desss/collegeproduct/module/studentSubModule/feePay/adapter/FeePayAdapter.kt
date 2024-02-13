package com.desss.collegeproduct.module.studentSubModule.feePay.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.databinding.AdapterStudentFeePayBinding
import com.desss.collegeproduct.module.studentSubModule.feePay.model.FeePayModel
import com.desss.collegeproduct.paymentGateway.PaymentScreen

class FeePayAdapter(private val context: Context?, private val feePayModelList: List<FeePayModel>) :
    RecyclerView.Adapter<FeePayAdapter.ViewHolder>() {

    private var feePayModel: FeePayModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterStudentFeePayBinding: AdapterStudentFeePayBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_student_fee_pay,
            parent,
            false
        )
        return ViewHolder(adapterStudentFeePayBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        feePayModel = feePayModelList[position]
        holder.binding.semesterContentTv.text = "Semester " + feePayModel!!.semester
        holder.binding.dateTv.text = "Date " + feePayModel!!.date
        holder.binding.receiptNoTv.text = "Receipt No: #123456789"

        if (feePayModel!!.status.equals("1") || feePayModel!!.status == "1") {
            holder.binding.feeStatusValueTv.text = "Paid"
            holder.binding.btnDownload.visibility = View.VISIBLE
            holder.binding.btnPayNow.visibility = View.GONE
        } else {
            holder.binding.feeStatusValueTv.text = "Not Paid"
            holder.binding.btnDownload.visibility = View.GONE
            holder.binding.btnPayNow.visibility = View.VISIBLE
        }

        holder.binding.btnDownload.setOnClickListener {
            CommonUtility.commonStartActivity(
                context as Activity,
                PaymentScreen::class.java,
                null,
                false
            )
        }
    }

    override fun getItemCount(): Int {
        return feePayModelList.size
    }

    class ViewHolder(binding: AdapterStudentFeePayBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterStudentFeePayBinding

        init {
            this.binding = binding
        }
    }
}