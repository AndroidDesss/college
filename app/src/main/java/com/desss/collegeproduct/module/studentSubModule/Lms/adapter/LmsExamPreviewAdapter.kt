package com.desss.collegeproduct.module.studentSubModule.Lms.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.AdapterExamPreviewBinding
import com.desss.collegeproduct.module.studentSubModule.Lms.model.QusAns


class LmsExamPreviewAdapter(private val context: Context?,private val questionAnswerMap: HashMap<Int, String>, private val questionList: List<QusAns>) :
    RecyclerView.Adapter<LmsExamPreviewAdapter.ViewHolder>() {

    private var qusAnsModel: QusAns? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterExamPreviewBinding: AdapterExamPreviewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_exam_preview,
            parent,
            false
        )
        return ViewHolder(adapterExamPreviewBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        qusAnsModel = questionList[position]
        val questionId = qusAnsModel!!.question_id
        holder.binding.questionContentTv.text = qusAnsModel!!.question_text
        holder.binding.selectedAnswerTv.text = questionAnswerMap[questionId]
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    class ViewHolder(binding: AdapterExamPreviewBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterExamPreviewBinding

        init {
            this.binding = binding
        }
    }
}