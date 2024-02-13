package com.desss.collegeproduct.module.professorSubModule.studentAttendance.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.AdapterNameListBinding

class NameListAdapter(private val context: Context?, private val nameList: List<String>) :
    RecyclerView.Adapter<NameListAdapter.ViewHolder>() {

    private var nameListModel: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterNameListBinding: AdapterNameListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_name_list,
            parent,
            false
        )
        return ViewHolder(adapterNameListBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        nameListModel = nameList[position]
        holder.binding.studentNameTv.text = nameListModel.toString()
    }

    override fun getItemCount(): Int {
        return nameList.size
    }

    class ViewHolder(binding: AdapterNameListBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterNameListBinding

        init {
            this.binding = binding
        }
    }
}