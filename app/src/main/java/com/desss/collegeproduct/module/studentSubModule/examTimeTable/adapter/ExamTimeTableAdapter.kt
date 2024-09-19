package com.desss.collegeproduct.module.studentSubModule.examTimeTable.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.Constants
import com.desss.collegeproduct.databinding.AdapterExamTimeTableBinding
import com.desss.collegeproduct.module.dashboard.DashBoardScreen
import com.desss.collegeproduct.module.studentSubModule.examTimeTable.model.ExamTimeTableModel
import kotlin.random.Random

class ExamTimeTableAdapter(
    private val context: Context?,
    private val examTimeTableList: List<ExamTimeTableModel>
) : RecyclerView.Adapter<ExamTimeTableAdapter.ViewHolder>() {


    private val sortedExamTimeTable: List<ExamTimeTableModel> = sortDocumentsByType(examTimeTableList)

    private var getCurrentExamType = ""

    private lateinit var downloadPdfUrl: String

    private lateinit var fileName: String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterExamTimeTableBinding: AdapterExamTimeTableBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_exam_time_table,
            parent,
            false
        )
        return ViewHolder(adapterExamTimeTableBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentExamTimeTableModel = sortedExamTimeTable[position]
        val getExamType = currentExamTimeTableModel.type
        if (getCurrentExamType.compareTo(getExamType) != 0) {
            getCurrentExamType = getExamType
            holder.binding.examTypeTv.visibility = View.VISIBLE
            holder.binding.examTypeTv.text = currentExamTimeTableModel.type + " Exam"
        } else {
            holder.binding.examTypeTv.visibility = View.VISIBLE
        }

        holder.binding.examNameTv.text = currentExamTimeTableModel.name
        holder.binding.examStartDateValueTv.text = currentExamTimeTableModel.date
        holder.binding.downloadTimeTable.setOnClickListener {
            if (checkAndHandleStoragePermission())
            {
                val currentExamTimeTableModels = sortedExamTimeTable[position]
                CommonUtility.showProgressDialog(context)
                downloadPdfUrl = Constants.timetableDownloadUrl + currentExamTimeTableModels.pdf
                fileName = currentExamTimeTableModels.name + System.currentTimeMillis() + ".pdf"
                val file = CommonUtility.createAppFolder(context!!)
                CommonUtility.downloadPdf(context, fileName, file.absolutePath, downloadPdfUrl)
            }
        }

    }

    override fun getItemCount(): Int {
        return sortedExamTimeTable.size
    }

    class ViewHolder(binding: AdapterExamTimeTableBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterExamTimeTableBinding

        init {
            this.binding = binding
        }
    }

    private fun checkAndHandleStoragePermission(): Boolean {
        if (CommonUtility.checkStoragePermission(context!!)) {
            return true
        } else {
            CommonUtility.requestStoragePermission(context as DashBoardScreen)
            return false
        }
    }

    private fun sortDocumentsByType(documents: List<ExamTimeTableModel>): List<ExamTimeTableModel> {
        val unitDocuments = documents.filter { it.type == "Unit" }
        val modelDocuments = documents.filter { it.type == "Model" }
        val semesterDocuments = documents.filter { it.type == "Semester" }
        return unitDocuments + modelDocuments + semesterDocuments
    }

}