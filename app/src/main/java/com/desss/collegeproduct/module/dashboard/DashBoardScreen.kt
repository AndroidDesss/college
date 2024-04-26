package com.desss.collegeproduct.module.dashboard

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.ActivityDashBoardScreenBinding
import com.desss.collegeproduct.module.auth.activity.LoginScreen
import com.desss.collegeproduct.module.commonDashBoardFragment.home.fragment.HomeFragmentScreen
import com.desss.collegeproduct.module.commonDashBoardFragment.notification.fragment.NotificationFragmentScreen
import com.desss.collegeproduct.module.commonDashBoardFragment.profile.fragment.ProfileFragmentScreen
import com.desss.collegeproduct.module.studentSubModule.Lms.fragment.LmsVideoExamFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashBoardScreen : AppCompatActivity() {

    private lateinit var dashBoardScreenBinding: ActivityDashBoardScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashBoardScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board_screen)
        hideMenuBasedOnUser()
        openFragment(HomeFragmentScreen())
        initListener()
    }

    private fun hideMenuBasedOnUser() {
        val menuItem: MenuItem = dashBoardScreenBinding.bottomNavigation.menu.findItem(R.id.profile)
        if (SharedPref.getRollId(this) == "2")
        {
            menuItem.isVisible = false
        }
        else
        {
            menuItem.isVisible = true
        }
    }

    private fun initListener() {
        dashBoardScreenBinding.bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
    }

    private var navigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    openFragment(HomeFragmentScreen())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    openFragment(ProfileFragmentScreen())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.notification -> {
                    openFragment(NotificationFragmentScreen())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun openFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment!!, null)
        transaction.addToBackStack(null)
        transaction.commit()
    }

//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed()
//    {
//        if (supportFragmentManager.backStackEntryCount == 1)
//        {
//            CommonUtility.showAlertDialog(this, "", "Are you sure you want to exit?", "Yes", "No",
//                object : CommonUtility.DialogClickListener {
//                    override fun dialogOkBtnClicked(value: String?) {
//                        CommonUtility.commonStartActivity(this@DashBoardScreen, LoginScreen::class.java,null,true)
//                    }
//                    override fun dialogNoBtnClicked(value: String?) {}
//                }
//            )
//        }
//        else
//        {
//            super.onBackPressed()
//        }
//    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment is LmsVideoExamFragment && !fragment.handleBackPressed()) {
            if (!(fragment).isFullscreen) {
                super.onBackPressed()
            }
        } else {
            if (supportFragmentManager.backStackEntryCount == 1) {
                CommonUtility.showAlertDialog(
                    this, "", "Are you sure you want to exit?", "Yes", "No",
                    object : CommonUtility.DialogClickListener {
                        override fun dialogOkBtnClicked(value: String?) {
                            CommonUtility.commonStartActivity(
                                this@DashBoardScreen,
                                LoginScreen::class.java,
                                null,
                                true
                            )
                        }
                        override fun dialogNoBtnClicked(value: String?) {}
                    }
                )
            } else {
                super.onBackPressed()
            }
        }
    }
}