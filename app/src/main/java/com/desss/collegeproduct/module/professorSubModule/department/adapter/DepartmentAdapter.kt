package com.desss.collegeproduct.module.professorSubModule.department.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.Constants
import com.desss.collegeproduct.databinding.AdapterDepartmentBinding
import com.desss.collegeproduct.module.professorSubModule.department.model.DepartmentModel
import kotlin.random.Random

class DepartmentAdapter(
    private val context: Context?,
    private val departmentList: List<DepartmentModel>
) : RecyclerView.Adapter<DepartmentAdapter.ViewHolder>() {

    private var departmentModel: DepartmentModel? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterDepartmentBinding: AdapterDepartmentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_department,
            parent,
            false
        )
        return ViewHolder(adapterDepartmentBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        departmentModel = departmentList[position]
        val randomColor =
            Color.parseColor(Constants.studentCategoriesBackGroundColor[Random.nextInt(Constants.studentCategoriesBackGroundColor.size)])
        holder.binding.startView.setBackgroundColor(randomColor)
        holder.binding.departmentTv.text = departmentModel!!.departement
        holder.binding.yearSectionValueTv.text =
            "Semester " + departmentModel!!.semester + "- Section " + departmentModel!!.section
        holder.binding.subjectValueTv.text = departmentModel!!.subject
    }

    override fun getItemCount(): Int {
        return departmentList.size
    }

    class ViewHolder(binding: AdapterDepartmentBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterDepartmentBinding

        init {
            this.binding = binding
        }
    }

}