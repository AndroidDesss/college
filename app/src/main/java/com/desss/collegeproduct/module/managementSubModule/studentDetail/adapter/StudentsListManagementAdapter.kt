package com.desss.collegeproduct.module.managementSubModule.studentDetail.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.databinding.AdapterStudentListBinding
import com.desss.collegeproduct.module.managementSubModule.studentDetail.fragment.StudentSubModuleDetailFragmentScreen
import com.desss.collegeproduct.module.managementSubModule.studentDetail.model.StudentListManagementBasedModel


class StudentsListManagementAdapter(
    private val context: Context?,
    private var studentList: List<StudentListManagementBasedModel>,
) : RecyclerView.Adapter<StudentsListManagementAdapter.ViewHolder>() {

    private var studentModel: StudentListManagementBasedModel? = null

    private var dataList: List<StudentListManagementBasedModel> = studentList.toList()
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
            val bundle = Bundle().apply {
                putString("studentId", studentModel!!.id)
            }
            val studentSubModuleDetailFragmentScreen = StudentSubModuleDetailFragmentScreen()
            CommonUtility.navigateToFragmentWithBundle(
                (context as FragmentActivity).supportFragmentManager,
                studentSubModuleDetailFragmentScreen,
                bundle,
                R.id.container,
                true
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