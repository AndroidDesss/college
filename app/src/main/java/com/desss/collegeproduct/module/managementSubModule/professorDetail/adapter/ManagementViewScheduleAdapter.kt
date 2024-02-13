package com.desss.collegeproduct.module.managementSubModule.professorDetail.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.Constants
import com.desss.collegeproduct.databinding.AdapterManagementScheduleBinding
import com.desss.collegeproduct.module.professorSubModule.schedule.model.ViewScheduleModel
import kotlin.random.Random

class ManagementViewScheduleAdapter(
    private val context: Context?,
    private var viewScheduleModelList: List<ViewScheduleModel>
) : RecyclerView.Adapter<ManagementViewScheduleAdapter.ViewHolder>() {

    private var viewScheduleModel: ViewScheduleModel? = null

    private var dataList: List<ViewScheduleModel> = viewScheduleModelList.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterManagementScheduleBinding: AdapterManagementScheduleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_management_schedule,
            parent,
            false
        )
        return ViewHolder(adapterManagementScheduleBinding)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewScheduleModel = viewScheduleModelList[position]
        val randomColor =
            Color.parseColor(Constants.studentCategoriesBackGroundColor[Random.nextInt(Constants.studentCategoriesBackGroundColor.size)])
        holder.binding.startView.setBackgroundColor(randomColor)
        holder.binding.classNameTv.text =
            viewScheduleModel!!.course + " " + viewScheduleModel!!.section
        holder.binding.scheduleContentTv.text =
            viewScheduleModel!!.from_time + "- " + viewScheduleModel!!.to_time
    }

    override fun getItemCount(): Int {
        return viewScheduleModelList.size
    }

    class ViewHolder(binding: AdapterManagementScheduleBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterManagementScheduleBinding

        init {
            this.binding = binding
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filter(query: String) {
        viewScheduleModelList = if (query.isEmpty()) {
            dataList
        } else {
            dataList.filter {
                it.course.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

}