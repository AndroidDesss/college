package com.desss.collegeproduct.module.studentSubModule.notes.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.Constants
import com.desss.collegeproduct.databinding.AdapterStudentNotesBinding
import com.desss.collegeproduct.module.studentSubModule.notes.model.NotesModel
import com.downloader.PRDownloader
import java.io.File
import java.nio.file.Path
import kotlin.random.Random

class NotesAdapter(private val context: Context?, private val notesModelList: List<NotesModel>) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private var notesModel: NotesModel? = null

    private lateinit var downloadPdfUrl: String

    private lateinit var fileName: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterStudentNotesBinding: AdapterStudentNotesBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_student_notes,
            parent,
            false
        )
        return ViewHolder(adapterStudentNotesBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        notesModel = notesModelList[position]

        val randomColor =
            Color.parseColor(Constants.studentCategoriesBackGroundColor[Random.nextInt(Constants.studentCategoriesBackGroundColor.size)])

        holder.binding.startView.setBackgroundColor(randomColor)
        holder.binding.professorNameTv.text =
            notesModel!!.course + " /Semester -" + notesModel!!.semester
        holder.binding.subjectNameTv.text = notesModel!!.name
        holder.binding.uploadedOnValueTv.text = notesModel!!.date
        holder.binding.fileNameTv.text = notesModel!!.name

        holder.binding.download.setOnClickListener {
            CommonUtility.showProgressDialog(context)
            notesModel = notesModelList[position]
            downloadPdfUrl = Constants.notesDownloadUrl + notesModel!!.pdf
            fileName = notesModel!!.name + System.currentTimeMillis() + ".pdf"
            val file = CommonUtility.createAppFolder(context!!)
            CommonUtility.downloadPdf(context, fileName, file.absolutePath, downloadPdfUrl)
        }

    }

    override fun getItemCount(): Int {
        return notesModelList.size
    }

    class ViewHolder(binding: AdapterStudentNotesBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterStudentNotesBinding

        init {
            this.binding = binding
        }
    }

}