package com.desss.collegeproduct.module.commonDashBoardFragment.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.FragmentHomeScreenBinding
import com.desss.collegeproduct.module.commonDashBoardFragment.home.adapter.CategoriesAdapter
import com.desss.collegeproduct.module.commonDashBoardFragment.home.model.CategoriesModel


class HomeFragmentScreen : Fragment() {

    private lateinit var fragmentHomeScreenBinding: FragmentHomeScreenBinding

    private val studentCategoriesArray = arrayOf("Fee Pay","Attendance","Syllabus","Exam Timetable","Remarks","Notes")

    private val studentCategoriesImage = arrayOf(R.drawable.fee_pay,R.drawable.student_attendance,R.drawable.student_syllabus,R.drawable.exam_timetable,R.drawable.student_remark,R.drawable.student_notes)

    private val studentCategoriesBackGroundColor = arrayOf("#8b88d0","#79b1fb","#fbb97c","#df90c9","#f7937e","#e5a9ac")

    private var categoriesList = mutableListOf<CategoriesModel>()

    private var categoriesAdapter: CategoriesAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View{
        fragmentHomeScreenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_screen, container, false)

        setCategoriesValues(studentCategoriesArray,studentCategoriesImage,studentCategoriesBackGroundColor)

        return fragmentHomeScreenBinding.root
    }

    private fun setCategoriesValues(nameArray:Array<String>,imageArray:Array<Int>,colorArray:Array<String>) {
        categoriesList.clear()
        for (i in 0 until nameArray.size)
        {
            categoriesList.add(CategoriesModel(nameArray[i],imageArray[i],colorArray[i]))
        }
        setBindingAdapter(categoriesList)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setBindingAdapter(categoriesModel: List<CategoriesModel>) {
        if (categoriesAdapter == null) {
            categoriesAdapter = CategoriesAdapter(context, categoriesModel)
//            val resId: Int = R.anim.layout_animation_fall_down
//            val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(context, resId)
//            fragmentHomeScreenBinding.recyclerView.setLayoutAnimation(animation)
            fragmentHomeScreenBinding.recyclerView.adapter = categoriesAdapter

            categoriesAdapter!!.notifyDataSetChanged()
        }
    }

}