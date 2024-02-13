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
import com.desss.collegeproduct.databinding.AdapterManagementDegreesBinding
import com.desss.collegeproduct.module.admission.activity.AdmissionCourseScreen
import com.desss.collegeproduct.module.admission.model.DegreeModel
import kotlin.random.Random


class AdmissionDegreeAdapter(
    private val context: Context?,
    private val degreeModelList: List<DegreeModel>
) : RecyclerView.Adapter<AdmissionDegreeAdapter.ViewHolder>() {

    private var degreeModel: DegreeModel? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterManagementDegreesBinding: AdapterManagementDegreesBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_management_degrees,
                parent,
                false
            )
        return ViewHolder(adapterManagementDegreesBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        degreeModel = degreeModelList[position]
        val randomColor =
            Color.parseColor(Constants.studentCategoriesBackGroundColor[Random.nextInt(Constants.studentCategoriesBackGroundColor.size)])
        holder.binding.backGroundLayout.setBackgroundColor(randomColor)
        holder.binding.degreeNameTv.text = degreeModel!!.name
        holder.binding.backGroundLayout.setOnClickListener { v ->
            degreeModel = degreeModelList[position]
            val intent = Intent(context, AdmissionCourseScreen::class.java).apply {
                putExtra("degreeId",degreeModel!!.id)
            }
            context!!.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return degreeModelList.size
    }

    class ViewHolder(binding: AdapterManagementDegreesBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterManagementDegreesBinding

        init {
            this.binding = binding
        }
    }

}