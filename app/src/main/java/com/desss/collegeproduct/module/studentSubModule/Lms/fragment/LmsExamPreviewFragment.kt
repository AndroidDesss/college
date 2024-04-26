package com.desss.collegeproduct.module.studentSubModule.Lms.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.FragmentLmsExamPreviewBinding
import com.desss.collegeproduct.module.studentSubModule.Lms.adapter.LmsExamPreviewAdapter
import com.desss.collegeproduct.module.studentSubModule.Lms.model.QusAns
import com.desss.collegeproduct.module.studentSubModule.Lms.viewModel.LmsLessonScreenViewModel

class LmsExamPreviewFragment : Fragment() {

    private lateinit var fragmentLmsExamPreviewBinding: FragmentLmsExamPreviewBinding

    private lateinit var userAnswersMap: HashMap<Int, String>

    private lateinit var questionList: List<QusAns>

    private var correctAnswersCount: Int = 0

    private var totalQuestions: Int = 0

    private var lmsExamPreviewAdapter: LmsExamPreviewAdapter? = null

    private lateinit var lmsLessonScreenViewModel: LmsLessonScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userAnswersMap = it.getSerializable("userAnswersMap") as HashMap<Int, String>
            questionList = it.getSerializable("questionList") as List<QusAns>
            correctAnswersCount = it.getInt("correctAnswersCount")
            totalQuestions = it.getInt("totalQuestions")
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
                CommonUtility.toastString("Thanks for attending the test..!", activity)
            }
        }
    }

//    private fun updateExamResults()
//    {
//        CommonUtility.showProgressDialog(context)
//        lmsLessonScreenViewModel.updateExamResults(
//            requireActivity(), "lms", SharedPref.getDegree(context).toString(),
//            SharedPref.getCourse(context).toString(), SharedPref.getSemester(context).toString()
//        )
//    }




}