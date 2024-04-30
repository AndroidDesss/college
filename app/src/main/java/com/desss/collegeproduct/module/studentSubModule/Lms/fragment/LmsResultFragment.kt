package com.desss.collegeproduct.module.studentSubModule.Lms.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.FragmentLmsResultBinding
import com.desss.collegeproduct.module.commonDashBoardFragment.home.fragment.HomeFragmentScreen

class LmsResultFragment : Fragment() {

    private lateinit var fragmentLmsResultBinding: FragmentLmsResultBinding

    private var correctAnswersCount: Int = 0

    private var totalQuestions: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            correctAnswersCount = it.getInt("correctAnswersCount")
            totalQuestions = it.getInt("totalQuestions")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentLmsResultBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_lms_result, container, false)
        setValues()
        initListener()
        return fragmentLmsResultBinding.root
    }

    private fun initListener() {
        fragmentLmsResultBinding.returnTv.setOnClickListener(onClickListener)
    }

    @SuppressLint("SetTextI18n")
    private fun setValues() {
        fragmentLmsResultBinding.scoreTv.text = "$correctAnswersCount/$totalQuestions"
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.returnTv ->  {
                navigateToDashboardScreen()
            }
        }
    }

    private fun navigateToDashboardScreen() {
        requireActivity().supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val homeFragment = HomeFragmentScreen()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
        transaction.commit()
    }


}