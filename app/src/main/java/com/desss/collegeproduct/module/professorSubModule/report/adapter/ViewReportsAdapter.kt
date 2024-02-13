package com.desss.collegeproduct.module.professorSubModule.report.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.Constants
import com.desss.collegeproduct.databinding.AdapterViewReportBinding
import com.desss.collegeproduct.module.professorSubModule.report.model.ViewReportsModel
import kotlin.random.Random


class ViewReportsAdapter(
    private val context: Context?,
    private val viewReportList: List<ViewReportsModel>
) : RecyclerView.Adapter<ViewReportsAdapter.ViewHolder>() {

    private var viewReportModel: ViewReportsModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterViewReportBinding: AdapterViewReportBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_view_report,
            parent,
            false
        )
        return ViewHolder(adapterViewReportBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewReportModel = viewReportList[position]
        val randomColor =
            Color.parseColor(Constants.studentCategoriesBackGroundColor[Random.nextInt(Constants.studentCategoriesBackGroundColor.size)])
        holder.binding.startView.setBackgroundColor(randomColor)
        holder.binding.studentNameTv.text = viewReportModel!!.name
        holder.binding.studentRegNoTv.text = viewReportModel!!.reg_no
        holder.binding.dateValueTv.text = viewReportModel!!.date

        //Read More and Read Less
        val originalText = viewReportModel!!.content
        val shortText = originalText.substring(0, 100) + "... Read More"
        var isFullTextShown = false
        val spannableString = SpannableStringBuilder(shortText)
        val readMoreSpan = ForegroundColorSpan(Color.BLUE)
        val readLessSpan = ForegroundColorSpan(Color.RED)
        spannableString.setSpan(readMoreSpan, shortText.indexOf("Read More"), shortText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.binding.reportsContentTv.text = spannableString
        holder.binding.reportsContentTv.setOnClickListener {
            isFullTextShown = !isFullTextShown
            if (isFullTextShown) {
                val fullText = originalText + " Read Less"
                val fullSpannableString = SpannableStringBuilder(fullText)
                fullSpannableString.setSpan(readLessSpan, fullText.indexOf("Read Less"), fullText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                holder.binding.reportsContentTv.text = fullSpannableString
            } else {
                holder.binding.reportsContentTv.text = spannableString
            }
        }


    }

    override fun getItemCount(): Int {
        return viewReportList.size
    }

    class ViewHolder(binding: AdapterViewReportBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterViewReportBinding

        init {
            this.binding = binding
        }
    }


}