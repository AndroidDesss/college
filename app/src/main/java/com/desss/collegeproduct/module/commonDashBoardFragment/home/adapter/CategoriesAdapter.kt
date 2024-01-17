package com.desss.collegeproduct.module.commonDashBoardFragment.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.AdapterCategoriesBinding
import com.desss.collegeproduct.module.commonDashBoardFragment.home.model.CategoriesModel

class CategoriesAdapter(private val context: Context?, private val categoriesModelList: List<CategoriesModel>) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private var categoriesModel: CategoriesModel? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val adapterCategoriesBinding: AdapterCategoriesBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.adapter_categories, parent, false)
        return ViewHolder(adapterCategoriesBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        categoriesModel = categoriesModelList[position]
        holder.binding.categoriesName.text = categoriesModel!!.categoriesName
        holder.binding.parentLayout.setBackgroundColor(android.graphics.Color.parseColor(categoriesModel!!.categoriesColor))
        holder.binding.categoriesImage.setImageResource(categoriesModel!!.categoriesImg)
    }

    override fun getItemCount(): Int {
        return categoriesModelList.size
    }


    class ViewHolder(binding: AdapterCategoriesBinding) : RecyclerView.ViewHolder(binding.getRoot())
    {
        val binding: AdapterCategoriesBinding

        init {
            this.binding = binding
        }
    }

}

