package com.desss.collegeproduct.module.studentSubModule.results.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.Constants
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.AdapterExamTimeTableBinding
import com.desss.collegeproduct.module.dashboard.DashBoardScreen
import com.desss.collegeproduct.module.studentSubModule.examTimeTable.model.ExamTimeTableModel
import com.desss.collegeproduct.module.studentSubModule.results.model.ResultsModel
import com.desss.collegeproduct.module.studentSubModule.results.viewModel.ExamResultsFragmentScreenViewModel
import kotlin.random.Random

class StudentResultsAdapter(
private val context: Context?,
private val examTimeTableList: List<ExamTimeTableModel>,
private val examResultsFragmentScreenViewModel: ExamResultsFragmentScreenViewModel
) : RecyclerView.Adapter<StudentResultsAdapter.ViewHolder>() {


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
            holder.binding.examTypeTv.visibility = View.GONE
        }

        holder.binding.examNameTv.text = currentExamTimeTableModel.name
        holder.binding.examStartDateValueTv.text = currentExamTimeTableModel.date
        holder.binding.downloadTimeTable.setOnClickListener {
            if (checkAndHandleStoragePermission())
            {
                val currentExamTimeTableModels = sortedExamTimeTable[position]
                checkResultsApi(currentExamTimeTableModels.id, holder)
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

    private fun checkResultsApi(timeTableId: String,viewHolder: StudentResultsAdapter.ViewHolder) {
        CommonUtility.showProgressDialog(context)
        examResultsFragmentScreenViewModel.callResultsApi(
            context as Activity,
            "read",
            "student_timetables_results",
            timeTableId,
            SharedPref.getId(context).toString()
        )
        observeViewModel(examResultsFragmentScreenViewModel,viewHolder)
    }

    private fun observeViewModel(
        viewModel: ExamResultsFragmentScreenViewModel,
        viewHolder: StudentResultsAdapter.ViewHolder
    ) {
        val lifecycleOwner = (viewHolder.itemView.context as? LifecycleOwner) ?: return
        viewModel.getResultsData()
            ?.observe(lifecycleOwner, Observer { resultsData ->
                if (resultsData != null) {
                    if (resultsData.status == 400 && resultsData.data.isNotEmpty()) {
                        CommonUtility.cancelProgressDialog(context)
                        CommonUtility.toastString("The results have not been updated yet.You will be able to download them once they have been updated.",context)
                    } else {
                        if (checkAndHandleStoragePermission())
                        {
                            val resultsDataList: List<ResultsModel> = resultsData.data
                            val userResults: ResultsModel? = resultsDataList.firstOrNull()
                            userResults?.let {
                                CommonUtility.showProgressDialog(context)
                                downloadPdfUrl = Constants.studentResultsDownloadUrl + it.pdf
                                fileName = "Results_" + System.currentTimeMillis() + ".pdf"
                                val file = CommonUtility.createAppFolder(context!!)
                                CommonUtility.downloadPdf(context, fileName, file.absolutePath, downloadPdfUrl)
                                CommonUtility.cancelProgressDialog(context)
                            }
                        }
                        else
                        {
                            CommonUtility.cancelProgressDialog(context)
                        }                    }
                } else {
                    CommonUtility.cancelProgressDialog(context)
                    CommonUtility.toastString("The results have not been updated yet.You will be able to download them once they have been updated.",context)
                }
            })
    }
}