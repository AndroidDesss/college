package com.desss.collegeproduct.module.studentSubModule.attendance.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.AdapterStudentAttendanceBinding
import com.desss.collegeproduct.module.studentSubModule.attendance.model.StudentAttendanceModel
import java.time.YearMonth

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

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        attendanceModel = attendanceModelList[position]
        holder.binding.monthTv.text = attendanceModel!!.Month + "  " + attendanceModel!!.Year
        holder.binding.workingDaysValueTv.text = getDaysInMonth(attendanceModel!!.Month,attendanceModel!!.Year,attendanceModel!!.holidayCount).toString()
        holder.binding.daysPresentValueTv.text = attendanceModel!!.count.toString()
    }

    override fun getItemCount(): Int {
        return attendanceModelList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDaysInMonth(month: String, year: String,holidayCount: Int): Int {
        val monthMap = mapOf(
            "January" to 1,
            "February" to 2,
            "March" to 3,
            "April" to 4,
            "May" to 5,
            "June" to 6,
            "July" to 7,
            "August" to 8,
            "September" to 9,
            "October" to 10,
            "November" to 11,
            "December" to 12
        )

        val monthNumber = monthMap[month.capitalize()] ?: throw IllegalArgumentException("Invalid month name")
        val yearInt = year.toIntOrNull() ?: throw IllegalArgumentException("Invalid year format")
        val yearMonth = YearMonth.of(yearInt, monthNumber)
        return yearMonth.lengthOfMonth() - holidayCount
    }

    class ViewHolder(binding: AdapterStudentAttendanceBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterStudentAttendanceBinding

        init {
            this.binding = binding
        }
    }

}