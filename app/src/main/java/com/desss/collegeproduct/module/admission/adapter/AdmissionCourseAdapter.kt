package com.desss.collegeproduct.module.admission.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.Constants
import com.desss.collegeproduct.databinding.AdapterManagementCourseBinding
import com.desss.collegeproduct.module.admission.activity.AdmissionFormScreen
import com.desss.collegeproduct.module.admission.model.CourseModel
import kotlin.random.Random


class AdmissionCourseAdapter(
    private val context: Context?,
    private val courseModelList: List<CourseModel>
) : RecyclerView.Adapter<AdmissionCourseAdapter.ViewHolder>() {

    private var courseModel: CourseModel? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterManagementCourseBinding: AdapterManagementCourseBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_management_course,
                parent,
                false
            )
        return ViewHolder(adapterManagementCourseBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        courseModel = courseModelList[position]
        val randomColor =
            Color.parseColor(Constants.studentCategoriesBackGroundColor[Random.nextInt(Constants.studentCategoriesBackGroundColor.size)])
        holder.binding.backGroundLayout.setBackgroundColor(randomColor)
        holder.binding.courseNameTv.text = courseModel!!.department
        holder.binding.yearValueTv.text = courseModel!!.year
        holder.binding.feeValueTv.text = courseModel!!.price
        holder.binding.backGroundLayout.setOnClickListener { v ->
            courseModel = courseModelList[position]
            val intent = Intent(context, AdmissionFormScreen::class.java).apply {
                putExtra("amsId",courseModel!!.ams_id)
            }
            context!!.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return courseModelList.size
    }

    class ViewHolder(binding: AdapterManagementCourseBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterManagementCourseBinding

        init {
            this.binding = binding
        }
    }

}