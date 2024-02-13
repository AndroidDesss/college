package com.desss.collegeproduct.module.commonDashBoardFragment.home.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.Constants
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.FragmentHomeScreenBinding
import com.desss.collegeproduct.module.auth.activity.LoginScreen
import com.desss.collegeproduct.module.commonDashBoardFragment.home.adapter.CategoriesAdapter
import com.desss.collegeproduct.module.commonDashBoardFragment.home.model.CategoriesModel


class HomeFragmentScreen : Fragment() {

    private lateinit var fragmentHomeScreenBinding: FragmentHomeScreenBinding

    private var categoriesList = mutableListOf<CategoriesModel>()

    private var categoriesAdapter: CategoriesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentHomeScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home_screen, container, false)
        checkUser()
        initListener()
        return fragmentHomeScreenBinding.root
    }

    private fun initListener() {
        fragmentHomeScreenBinding.logOut.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.logOut ->
                CommonUtility.showAlertDialog(context,
                    "",
                    "Are you sure you want to exit?",
                    "Yes",
                    "No",
                    object : CommonUtility.DialogClickListener {
                        override fun dialogOkBtnClicked(value: String?) {
                            CommonUtility.commonStartActivity(
                                activity!!,
                                LoginScreen::class.java,
                                null,
                                true
                            )
                        }

                        override fun dialogNoBtnClicked(value: String?) {}
                    }
                )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkUser() {
        if (SharedPref.getRollId(context) == "4") //Student
        {
            CommonUtility.showProgressDialog(context)
            fragmentHomeScreenBinding.logOut.isVisible = false
            fragmentHomeScreenBinding.dashBoardTv.text = "Student DashBoard"
            setCategoriesValues(
                Constants.studentCategoriesArray,
                Constants.studentCategoriesImage,
                Constants.studentCategoriesBackGroundColor
            )
        } else if (SharedPref.getRollId(context) == "3") //Professor
        {
            CommonUtility.showProgressDialog(context)
            fragmentHomeScreenBinding.logOut.isVisible = false
            fragmentHomeScreenBinding.dashBoardTv.text = "Professor DashBoard"
            setCategoriesValues(
                Constants.professorCategoriesArray,
                Constants.professorCategoriesImage,
                Constants.professorCategoriesBackGroundColor
            )
        }else if (SharedPref.getRollId(context) == "2") //Management DashBoard
        {
            CommonUtility.showProgressDialog(context)
            fragmentHomeScreenBinding.logOut.isVisible = true
            fragmentHomeScreenBinding.dashBoardTv.text = "Management DashBoard"
            setCategoriesValues(
                Constants.managementCategoriesArray,
                Constants.managementCategoriesImage,
                Constants.managementCategoriesBackGroundColor
            )
        }
    }

    private fun setCategoriesValues(
        nameArray: Array<String>,
        imageArray: Array<Int>,
        colorArray: Array<String>
    ) {
        categoriesList.clear()
        for (i in 0 until nameArray.size) {
            categoriesList.add(CategoriesModel(nameArray[i], imageArray[i], colorArray[i]))
        }
        setBindingAdapter(categoriesList)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun setBindingAdapter(categoriesModel: List<CategoriesModel>) {
        categoriesAdapter = CategoriesAdapter(context, categoriesModel)
        fragmentHomeScreenBinding.recyclerView.adapter = categoriesAdapter
        categoriesAdapter!!.notifyDataSetChanged()
        fragmentHomeScreenBinding.nameValueTv.text =
            SharedPref.getFirstName(context) + " " + SharedPref.getLastName(context)
        fragmentHomeScreenBinding.userIdValueTv.text = SharedPref.getEmailId(context)
        CommonUtility.cancelProgressDialog(context)
    }


}