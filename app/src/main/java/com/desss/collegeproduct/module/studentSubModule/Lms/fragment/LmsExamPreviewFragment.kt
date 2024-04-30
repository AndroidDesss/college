package com.desss.collegeproduct.module.studentSubModule.Lms.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.FragmentLmsExamPreviewBinding
import com.desss.collegeproduct.module.commonDashBoardFragment.home.fragment.HomeFragmentScreen
import com.desss.collegeproduct.module.dashboard.DashBoardScreen
import com.desss.collegeproduct.module.studentSubModule.Lms.adapter.LmsExamPreviewAdapter
import com.desss.collegeproduct.module.studentSubModule.Lms.model.QusAns
import com.desss.collegeproduct.module.studentSubModule.Lms.viewModel.LmsLessonScreenViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject

class LmsExamPreviewFragment : Fragment() {

    private lateinit var fragmentLmsExamPreviewBinding: FragmentLmsExamPreviewBinding

    private lateinit var userAnswersMap: HashMap<Int, String>

    private lateinit var questionList: List<QusAns>

    private var correctAnswersCount: Int = 0

    private var totalQuestions: Int = 0

    private lateinit var lessonId: String

    private lateinit var questionAnswersJson: String

    private var lmsExamPreviewAdapter: LmsExamPreviewAdapter? = null

    private lateinit var lmsLessonScreenViewModel: LmsLessonScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userAnswersMap = it.getSerializable("userAnswersMap") as HashMap<Int, String>
            questionList = it.getSerializable("questionList") as List<QusAns>
            correctAnswersCount = it.getInt("correctAnswersCount")
            totalQuestions = it.getInt("totalQuestions")
            lessonId = it.getString("lessonId").toString()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentLmsExamPreviewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_lms_exam_preview, container, false)
        setBindingAdapter()
        initViewModel()
        initListener()
        return fragmentLmsExamPreviewBinding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setBindingAdapter() {
        lmsExamPreviewAdapter = LmsExamPreviewAdapter(context, userAnswersMap, questionList)
        fragmentLmsExamPreviewBinding.recyclerView.adapter = lmsExamPreviewAdapter
        lmsExamPreviewAdapter!!.notifyDataSetChanged()
    }

    private fun initViewModel() {
        lmsLessonScreenViewModel =
            LmsLessonScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun initListener() {
        fragmentLmsExamPreviewBinding.submitButton.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.submitButton ->  {
                val gson = Gson()
                val jsonArray = ArrayList<JsonObject>()
                userAnswersMap.forEach { (questionId, answer) ->
                    val questionText = questionList.find { it.question_id == questionId }?.question_text ?: ""
                    val jsonObject = JsonObject().apply {
                        addProperty("question_id", questionId)
                        addProperty("question_text", questionText)
                        addProperty("answer", answer)
                    }
                    jsonArray.add(jsonObject)
                }
                questionAnswersJson = gson.toJson(jsonArray)
                updateExamResults()
                observeViewModel(lmsLessonScreenViewModel)
            }
        }
    }

    private fun updateExamResults()
    {
        CommonUtility.showProgressDialog(context)
        lmsLessonScreenViewModel.callLmsPostExamUpdateApi(
            requireActivity(), "update_lms", SharedPref.getId(context).toString(),lessonId,totalQuestions.toString(),
            correctAnswersCount.toString(), questionAnswersJson)
    }

    private fun observeViewModel(viewModel: LmsLessonScreenViewModel) {
        viewModel.getLmsPostExamUpdateData()?.observe(requireActivity(), Observer { lmsData ->
            if (lmsData != null) {
                if (lmsData.status == 403 && lmsData.data.isNotEmpty()) {
                    CommonUtility.cancelProgressDialog(activity)
                } else {
                    CommonUtility.cancelProgressDialog(activity)
                    CommonUtility.toastString("Thanks for attending the test..!", activity)
                    val bundle = Bundle().apply {
                        putInt("correctAnswersCount", correctAnswersCount)
                        putInt("totalQuestions", totalQuestions)
                    }
                    val lmsResultFragment  = LmsResultFragment()
                    lmsResultFragment.arguments = bundle
                    CommonUtility.navigateToFragment(
                        (context as FragmentActivity).supportFragmentManager,
                        lmsResultFragment,
                        R.id.container,
                        true
                    )
                }
            } else {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
    }

}