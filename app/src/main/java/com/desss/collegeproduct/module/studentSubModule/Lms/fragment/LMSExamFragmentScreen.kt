package com.desss.collegeproduct.module.studentSubModule.Lms.fragment


import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.FragmentLMSExamScreenBinding
import com.desss.collegeproduct.module.studentSubModule.Lms.model.LmsModel
import com.desss.collegeproduct.module.studentSubModule.Lms.model.QusAns
import com.desss.collegeproduct.module.studentSubModule.Lms.viewModel.LmsLessonScreenViewModel

class LMSExamFragmentScreen : Fragment() {

    private lateinit var fragmentLMSExamScreenBinding:FragmentLMSExamScreenBinding

    private lateinit var lmsLessonScreenViewModel: LmsLessonScreenViewModel

    private var currentQuestionIndex = 0

    private var totalQuestions: Int = 0

    private val userAnswersMap = HashMap<Int, String>()

    private val correctAnswersMap = HashMap<Int, String>()

    private var questionList = listOf<QusAns>()

    private lateinit var lessonId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lessonId = it.getString("lessonId").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentLMSExamScreenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_l_m_s_exam_screen, container, false)
        initViewModel()
        callApi()
        observeViewModel(lmsLessonScreenViewModel)
        initListener()
        return fragmentLMSExamScreenBinding.root
    }

    private fun initViewModel() {
        lmsLessonScreenViewModel =
            LmsLessonScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        lmsLessonScreenViewModel.callLmsSingleLessonApi(
            requireActivity(), "lms", SharedPref.getDegree(context).toString(),
            SharedPref.getCourse(context).toString(), SharedPref.getSemester(context).toString(),
            lessonId
        )
    }

    private fun observeViewModel(viewModel: LmsLessonScreenViewModel) {

        viewModel.getLmsSingleLessonData()?.observe(requireActivity(), Observer { lmsData ->
            if (lmsData != null) {
                if (lmsData.status == 403 && lmsData.data.isNotEmpty()) {
                    CommonUtility.cancelProgressDialog(activity)
                } else {
                    CommonUtility.cancelProgressDialog(activity)
                    handleLmsLessonData(lmsData)
                }
            } else {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
    }

    private fun handleLmsLessonData(response: CommonResponseModel<LmsModel>) {
        if (response.status == 200) {
            val lmsModel: LmsModel? = response.data.firstOrNull()
            if (lmsModel != null) {
                questionList = lmsModel.qus_ans
                totalQuestions = questionList.size

                if (currentQuestionIndex >= 0 && currentQuestionIndex < questionList.size) {
                    val currentQuestion: QusAns = questionList[currentQuestionIndex]
                    val questionText: String = currentQuestion.question_text
                    val options: List<String> = currentQuestion.options

                    // Update the question TextView with the current question text
                    fragmentLMSExamScreenBinding.questionTextView.text = questionText

                    val questionNumberText = "Question ${currentQuestionIndex + 1} of ${questionList.size}"
                    fragmentLMSExamScreenBinding.questionNumberTextView.text = questionNumberText

                    // Clear previous options if any
                    fragmentLMSExamScreenBinding.optionsRadioGroup.removeAllViews()

                    // Add RadioButtons for options
                    for (option in options) {
                        val radioButton = RadioButton(requireContext())
                        radioButton.text = option
                        radioButton.setTextColor(resources.getColor(R.color.black))
                        val typeface = ResourcesCompat.getFont(requireContext(), R.font.regular)
                        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                        radioButton.typeface = typeface


                        fragmentLMSExamScreenBinding.optionsRadioGroup.addView(radioButton)

                        // Check the RadioButton if it matches the user's saved answer
                        val questionId = currentQuestion.question_id
                        if (option == userAnswersMap[questionId]) {
                            radioButton.isChecked = true
                        }

                        // RadioGroup option selection listener
                        radioButton.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                // Update user's answer in the map
                                userAnswersMap[questionId] = option
                            }
                        }
                    }
                }

                for (question in questionList) {
                    correctAnswersMap[question.question_id] = question.correct_answer
                }
            }
        }
    }

    private fun initListener() {
        fragmentLMSExamScreenBinding.previousButton.setOnClickListener(onClickListener)
        fragmentLMSExamScreenBinding.nextButton.setOnClickListener(onClickListener)
        fragmentLMSExamScreenBinding.finishTextView.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.previousButton ->  {
                moveToPreviousQuestion()
            }
            R.id.nextButton ->  {
                moveToNextQuestion()
            }
            R.id.finishTextView ->  {
                if (allQuestionsAnswered(userAnswersMap, totalQuestions)) {
                    var correctAnswersCount = 0
                    for ((questionId, userAnswer) in userAnswersMap) {
                        val correctAnswer = correctAnswersMap[questionId]
                        if (userAnswer == correctAnswer) {
                            correctAnswersCount++
                        }
                    }

                    val bundle = Bundle().apply {
                        putSerializable("userAnswersMap", userAnswersMap)
                        putSerializable("questionList", ArrayList(questionList))
                        putInt("correctAnswersCount", correctAnswersCount)
                        putInt("totalQuestions", totalQuestions)
                    }
                    val lmsExamPreviewFragment  = LmsExamPreviewFragment()
                    lmsExamPreviewFragment.arguments = bundle
                    CommonUtility.navigateToFragment(
                        (context as FragmentActivity).supportFragmentManager,
                        lmsExamPreviewFragment,
                        R.id.container,
                        true
                    )
                } else {
                    CommonUtility.toastString("Please answer all questions..!", activity)
                }
            }
        }
    }
    private fun moveToNextQuestion() {
        val totalQuestions = lmsLessonScreenViewModel.getLmsSingleLessonData()?.value?.data?.firstOrNull()?.qus_ans?.size ?: return
        if (currentQuestionIndex < totalQuestions - 1) {
            currentQuestionIndex++
            updateQuestionUI()
        }
    }

    private fun moveToPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--
            updateQuestionUI()
        }
    }

    private fun updateQuestionUI() {
        val lmsData = lmsLessonScreenViewModel.getLmsSingleLessonData()?.value

        if (lmsData != null && lmsData.status == 200 && lmsData.data.isNotEmpty()) {
            val lmsModel = lmsData.data.first()
            questionList = lmsModel.qus_ans

            if (currentQuestionIndex >= 0 && currentQuestionIndex < questionList.size) {
                val currentQuestion = questionList[currentQuestionIndex]
                val questionText = currentQuestion.question_text
                val options = currentQuestion.options

                // Update the question TextView with the current question text
                fragmentLMSExamScreenBinding.questionTextView.text = questionText

                val questionNumberText = "Question ${currentQuestionIndex + 1} of ${questionList.size}"
                fragmentLMSExamScreenBinding.questionNumberTextView.text = questionNumberText

                // Clear previous options if any
                fragmentLMSExamScreenBinding.optionsRadioGroup.removeAllViews()

                // Add RadioButtons for options
                for (option in options) {
                    val radioButton = RadioButton(requireContext())
                    radioButton.text = option
                    radioButton.setTextColor(resources.getColor(R.color.black))
                    val typeface = ResourcesCompat.getFont(requireContext(), R.font.regular)
                    radioButton.typeface = typeface
                    radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                    fragmentLMSExamScreenBinding.optionsRadioGroup.addView(radioButton)

                    // Check the RadioButton if it matches the user's saved answer
                    val questionId = currentQuestion.question_id
                    if (option == userAnswersMap[questionId]) {
                        radioButton.isChecked = true
                    }

                    // RadioGroup option selection listener
                    radioButton.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            // Update user's answer in the map
                            userAnswersMap[questionId] = option
                        }
                    }
                }
            }
            for (question in questionList) {
                correctAnswersMap[question.question_id] = question.correct_answer
            }
        }
    }

    private fun allQuestionsAnswered(userAnswersMap: HashMap<Int, String>, totalQuestions: Int): Boolean {
        if (userAnswersMap.size != totalQuestions) {
            return false
        }
        for ((_, answer) in userAnswersMap) {
            if (answer.isNullOrEmpty()) {
                return false
            }
        }
        return true
    }

}