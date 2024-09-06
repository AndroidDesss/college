package com.desss.collegeproduct.module.studentSubModule.attendance.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import kotlin.random.Random
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.Constants
import com.desss.collegeproduct.databinding.AdapterStudentAttendanceBinding
import com.desss.collegeproduct.module.studentSubModule.attendance.model.StudentAttendanceModel

class StudentAttendanceAdapter(
    private val context: Context?,
    private val attendanceModelList: List<StudentAttendanceModel>
) : RecyclerView.Adapter<StudentAttendanceAdapter.ViewHolder>() {

    private var attendanceModel: StudentAttendanceModel? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterStudentAttendanceBinding: AdapterStudentAttendanceBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_student_attendance,
                parent,
                false
            )
        return ViewHolder(adapterStudentAttendanceBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        attendanceModel = attendanceModelList[position]
        holder.binding.monthTv.text = attendanceModel!!.Month + "  " + attendanceModel!!.Year
        holder.binding.workingDaysValueTv.text = attendanceModel!!.all_count.toString()
        holder.binding.daysPresentValueTv.text = attendanceModel!!.count.toString()
    }

    override fun getItemCount(): Int {
        return attendanceModelList.size
    }

    class ViewHolder(binding: AdapterStudentAttendanceBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterStudentAttendanceBinding

        init {
            this.binding = binding
        }
    }

}