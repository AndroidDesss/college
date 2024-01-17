package com.desss.collegeproduct.module.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.ActivityDashBoardScreenBinding
import com.desss.collegeproduct.module.commonDashBoardFragment.home.HomeFragmentScreen
import com.desss.collegeproduct.module.commonDashBoardFragment.notification.NotificationFragmentScreen
import com.desss.collegeproduct.module.commonDashBoardFragment.profile.ProfileFragmentScreen
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashBoardScreen : AppCompatActivity() {

    private lateinit var dashBoardScreenBinding: ActivityDashBoardScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashBoardScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board_screen)
        openFragment(HomeFragmentScreen())
        initListener()
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
}