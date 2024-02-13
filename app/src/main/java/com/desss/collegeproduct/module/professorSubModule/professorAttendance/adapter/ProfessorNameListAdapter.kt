package com.desss.collegeproduct.module.professorSubModule.professorAttendance.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.Constants
import com.desss.collegeproduct.databinding.AdapterProfessorAttendanceCountBinding
import kotlin.random.Random


class ProfessorNameListAdapter(private val context: Context?, private val nameList: List<String>) :
    RecyclerView.Adapter<ProfessorNameListAdapter.ViewHolder>() {

    private var nameListModel: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterProfessorAttendanceCountBinding: AdapterProfessorAttendanceCountBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_professor_attendance_count,
                parent,
                false
            )
        return ViewHolder(adapterProfessorAttendanceCountBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        nameListModel = nameList[position]
        val subMonthYearTime = nameListModel!!.split("&&")
        val randomColor =
            Color.parseColor(Constants.studentCategoriesBackGroundColor[Random.nextInt(Constants.studentCategoriesBackGroundColor.size)])
        holder.binding.startView.setBackgroundColor(randomColor)
        holder.binding.dateYearTv.text = subMonthYearTime[0]
        holder.binding.timeTv.text = subMonthYearTime[1]
    }

    override fun getItemCount(): Int {
        return nameList.size
    }

    class ViewHolder(binding: AdapterProfessorAttendanceCountBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterProfessorAttendanceCountBinding

        init {
            this.binding = binding
        }
    }
}