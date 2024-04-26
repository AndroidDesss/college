package com.desss.collegeproduct.module.studentSubModule.Lms.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.Constants
import com.desss.collegeproduct.databinding.AdapterLmsLessonBinding
import com.desss.collegeproduct.module.studentSubModule.Lms.fragment.LmsVideoExamFragment
import com.desss.collegeproduct.module.studentSubModule.Lms.model.LmsModel
import kotlin.random.Random

class LmsLessonAdapter(private val context: Context?, private val lmsLessonList: List<LmsModel>) :
    RecyclerView.Adapter<LmsLessonAdapter.ViewHolder>() {

    private var lmsLessonModel: LmsModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterLmsLessonBinding: AdapterLmsLessonBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_lms_lesson,
            parent,
            false
        )
        return ViewHolder(adapterLmsLessonBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        lmsLessonModel = lmsLessonList[position]

        holder.binding.lessonContentTv.text =  lmsLessonModel!!.title
        val randomColor = Color.parseColor(Constants.studentCategoriesBackGroundColor[Random.nextInt(Constants.studentCategoriesBackGroundColor.size)])
        holder.binding.childLayout.setBackgroundColor(randomColor)

        holder.binding.infoCardView.setOnClickListener {
            val videoUrls = getVideoUrls(lmsLessonList)
            val lessonIds = getLessonIds(lmsLessonList)
            val bundle = Bundle().apply {
                putStringArrayList("videoUrls", ArrayList(videoUrls))
                putStringArrayList("lessonIds", ArrayList(lessonIds))
                putInt("position", position)
            }
            val lmsVideoExamFragment  = LmsVideoExamFragment()
            lmsVideoExamFragment.arguments = bundle
            CommonUtility.navigateToFragment(
                (context as FragmentActivity).supportFragmentManager,
                lmsVideoExamFragment,
                R.id.container,
                true
            )
        }
    }

    override fun getItemCount(): Int {
        return lmsLessonList.size
    }

    class ViewHolder(binding: AdapterLmsLessonBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterLmsLessonBinding

        init {
            this.binding = binding
        }
    }

    private fun getVideoUrls(lmsLessonList: List<LmsModel>): List<String> {
        val videoUrls = mutableListOf<String>()
        for (lesson in lmsLessonList) {
            videoUrls.add(lesson.url)
        }
        return videoUrls
    }

    private fun getLessonIds(lmsLessonList: List<LmsModel>): List<String> {
        val lessonId = mutableListOf<String>()
        for (lesson in lmsLessonList) {
            lessonId.add(lesson.id)
        }
        return lessonId
    }
}