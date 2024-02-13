package com.desss.collegeproduct.module.professorSubModule.studentAttendance.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.AdapterStudentAttendanceEntryListBinding
import com.desss.collegeproduct.module.professorSubModule.report.model.StudentListBasedModel

class AttendanceStudentsListAdapter(
    private val context: Context?,
    private var studentList: List<StudentListBasedModel>
) : RecyclerView.Adapter<AttendanceStudentsListAdapter.ViewHolder>() {

    private var studentModel: StudentListBasedModel? = null

    private val totalStudentsList: ArrayList<String> = ArrayList()

    private var dataList: List<StudentListBasedModel> = studentList.toList()

    init {
        for (item in studentList) {
            totalStudentsList.add(item.id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterStudentAttendanceEntryListBinding: AdapterStudentAttendanceEntryListBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_student_attendance_entry_list,
                parent,
                false
            )
        return ViewHolder(adapterStudentAttendanceEntryListBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        studentModel = studentList[position]
        holder.binding.studentNameTv.text =
            studentModel!!.first_name + " " + studentModel!!.last_name
        holder.binding.switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
            studentModel = studentList[position]
            if (totalStudentsList.contains(studentModel!!.id)) {
                totalStudentsList.remove(studentModel!!.id)
            } else {
                totalStudentsList.add(studentModel!!.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    class ViewHolder(binding: AdapterStudentAttendanceEntryListBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterStudentAttendanceEntryListBinding

        init {
            this.binding = binding
        }
    }

    fun totalStudentCountPresentList(): List<String> {
        return totalStudentsList
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filter(query: String) {
        studentList = if (query.isEmpty()) {
            dataList
        } else {
            dataList.filter {
                it.first_name.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}