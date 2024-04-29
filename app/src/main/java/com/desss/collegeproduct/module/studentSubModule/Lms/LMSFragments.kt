package com.desss.collegeproduct.module.studentSubModule.Lms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.FragmentLMSFragmentsBinding
import com.desss.collegeproduct.module.studentSubModule.Lms.fragment.LMSExamFragmentScreen
import com.desss.collegeproduct.module.studentSubModule.Lms.fragment.LmsVideoFragment


class LMSFragments : Fragment() {

    private lateinit var fragmentLMSFragmentsBinding: FragmentLMSFragmentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentLMSFragmentsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_l_m_s_fragments, container, false)
        initListener()
        return fragmentLMSFragmentsBinding.root
    }

    private fun initListener() {
        fragmentLMSFragmentsBinding.btnZoom.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btnZoom ->  {
                if (SharedPref.getCourse(context) == "yes")
                {
                    CommonUtility.showAlertDialog(context, "", "Are you sure you want to watch the course again?", "Yes", "No",
                        object : CommonUtility.DialogClickListener {
                            override fun dialogOkBtnClicked(value: String?) {
                                val lmsVideoExamFragment = LmsVideoFragment()
                                CommonUtility.navigateToFragment(
                                    (context as FragmentActivity).supportFragmentManager,
                                    lmsVideoExamFragment,
                                    R.id.container,
                                    true
                                )
                            }
                            override fun dialogNoBtnClicked(value: String?) {
                                val lmsExamFragmentScreen = LMSExamFragmentScreen()
                                CommonUtility.navigateToFragment(
                                    (context as FragmentActivity).supportFragmentManager,
                                    lmsExamFragmentScreen,
                                    R.id.container,
                                    true
                                )
                            }
                        }
                    )
                }
                else
                {
                    val lmsVideoExamFragment = LmsVideoFragment()
                    CommonUtility.navigateToFragment(
                        (context as FragmentActivity).supportFragmentManager,
                        lmsVideoExamFragment,
                        R.id.container,
                        true
                    )

//                    val expoPlayerFragmentScreen = ExpoPlayerFragmentScreen()
//                    CommonUtility.navigateToFragment(
//                        (context as FragmentActivity).supportFragmentManager,
//                        expoPlayerFragmentScreen,
//                        R.id.container,
//                        true
//                    )
                }
            }
        }
    }

}