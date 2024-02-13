package com.desss.collegeproduct.module.studentSubModule.remarks.adapter

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
import com.desss.collegeproduct.databinding.AdapterStudentRemarksBinding
import com.desss.collegeproduct.module.studentSubModule.remarks.model.RemarksModel
import kotlin.random.Random


class RemarksAdapter(
    private val context: Context?,
    private val remarksModelList: List<RemarksModel>
) : RecyclerView.Adapter<RemarksAdapter.ViewHolder>() {

    private var remarksModel: RemarksModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterStudentRemarksBinding: AdapterStudentRemarksBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_student_remarks,
            parent,
            false
        )
        return ViewHolder(adapterStudentRemarksBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        remarksModel = remarksModelList[position]
        val randomColor =
            Color.parseColor(Constants.studentCategoriesBackGroundColor[Random.nextInt(Constants.studentCategoriesBackGroundColor.size)])
        holder.binding.startView.setBackgroundColor(randomColor)
        holder.binding.monthTv.text = remarksModel!!.date

        //Read More and Read Less
        val originalText = remarksModel!!.content
        val shortText = originalText.substring(0, 100) + "... Read More"
        var isFullTextShown = false
        val spannableString = SpannableStringBuilder(shortText)
        val readMoreSpan = ForegroundColorSpan(Color.BLUE)
        val readLessSpan = ForegroundColorSpan(Color.RED)
        spannableString.setSpan(readMoreSpan, shortText.indexOf("Read More"), shortText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.binding.remarksTv.text = spannableString
        holder.binding.remarksTv.setOnClickListener {
            isFullTextShown = !isFullTextShown
            if (isFullTextShown) {
                val fullText = originalText + " Read Less"
                val fullSpannableString = SpannableStringBuilder(fullText)
                fullSpannableString.setSpan(readLessSpan, fullText.indexOf("Read Less"), fullText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                holder.binding.remarksTv.text = fullSpannableString
            } else {
                holder.binding.remarksTv.text = spannableString
            }
        }
    }

    override fun getItemCount(): Int {
        return remarksModelList.size
    }

    class ViewHolder(binding: AdapterStudentRemarksBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterStudentRemarksBinding

        init {
            this.binding = binding
        }
    }

}