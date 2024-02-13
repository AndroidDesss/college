package com.desss.collegeproduct.module.managementSubModule.professorDetail.adapter

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
import com.desss.collegeproduct.module.managementSubModule.professorDetail.fragment.ProfessorSubModuleDetailFragment
import com.desss.collegeproduct.module.managementSubModule.professorDetail.model.TotalProfessorModel

class TotalProfessorAdapter(
    private val context: Context?,
    private var professorList: List<TotalProfessorModel>,
) : RecyclerView.Adapter<TotalProfessorAdapter.ViewHolder>() {

    private var professorModel: TotalProfessorModel? = null

    private var dataList: List<TotalProfessorModel> = professorList.toList()
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
        professorModel = professorList[position]
        holder.binding.studentNameTv.text =
            professorModel!!.first_name + " " + professorModel!!.last_name
        holder.binding.editIcon.setOnClickListener { v ->
            professorModel = professorList[position]
            val bundle = Bundle().apply {
                putString("professorId", professorModel!!.id)
            }
            val professorSubModuleDetailFragmentScreen = ProfessorSubModuleDetailFragment()
            CommonUtility.navigateToFragmentWithBundle(
                (context as FragmentActivity).supportFragmentManager,
                professorSubModuleDetailFragmentScreen,
                bundle,
                R.id.container,
                true
            )
        }
    }

    override fun getItemCount(): Int {
        return professorList.size
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
        professorList = if (query.isEmpty()) {
            dataList
        } else {
            dataList.filter { it.first_name.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

}