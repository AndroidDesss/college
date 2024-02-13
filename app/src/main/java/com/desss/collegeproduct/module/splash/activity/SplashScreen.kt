package com.desss.collegeproduct.module.splash.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.databinding.ActivitySplashScreenBinding
import com.desss.collegeproduct.module.auth.activity.LoginScreen
import com.desss.collegeproduct.module.splash.SplashScreenViewModel

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private lateinit var splashScreenBinding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        moveToLogin()
    }

    private fun moveToLogin() {
        Handler().postDelayed({ startIntent() }, 1500)
    }

    private fun startIntent() {
        CommonUtility.Companion.commonStartActivity(this, LoginScreen::class.java, null, true)
    }
}