package com.desss.collegeproduct.module.studentSubModule.syllabus.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.Constants
import com.desss.collegeproduct.databinding.AdapterStudentSyllabusBinding
import com.desss.collegeproduct.module.studentSubModule.syllabus.model.SyllabusModel
import kotlin.random.Random


class SyllabusAdapter(
    private val context: Context?,
    private val syllabusModelList: List<SyllabusModel>
) : RecyclerView.Adapter<SyllabusAdapter.ViewHolder>() {

    private var syllabusModel: SyllabusModel? = null

    private lateinit var downloadPdfUrl: String

    private lateinit var fileName: String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterStudentSyllabusBinding: AdapterStudentSyllabusBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_student_syllabus,
            parent,
            false
        )
        return ViewHolder(adapterStudentSyllabusBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        syllabusModel = syllabusModelList[position]
        val randomColor =
            Color.parseColor(Constants.studentCategoriesBackGroundColor[Random.nextInt(Constants.studentCategoriesBackGroundColor.size)])
        holder.binding.startView.setBackgroundColor(randomColor)
        holder.binding.syllabusTv.text = syllabusModel!!.name
        holder.binding.downloadSyllabus.setOnClickListener {
            syllabusModel = syllabusModelList[position]
            CommonUtility.showProgressDialog(context)
            downloadPdfUrl = Constants.syllabusDownloadUrl + syllabusModel!!.pdf
            fileName = syllabusModel!!.name + System.currentTimeMillis() + ".pdf"
            val file = CommonUtility.createAppFolder(context!!)
            CommonUtility.downloadPdf(context, fileName, file.absolutePath, downloadPdfUrl)
        }

    }

    override fun getItemCount(): Int {
        return syllabusModelList.size
    }

    class ViewHolder(binding: AdapterStudentSyllabusBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterStudentSyllabusBinding

        init {
            this.binding = binding
        }
    }

}