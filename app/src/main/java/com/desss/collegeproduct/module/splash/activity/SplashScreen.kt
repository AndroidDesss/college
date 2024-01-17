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
    private lateinit var splashScreenViewModel: SplashScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        splashScreenViewModel = SplashScreenViewModel(application, this)
        moveToLogin()
        CommonUtility.Companion.toastString("Hi",this)
    }

    private fun moveToLogin() {
        Handler().postDelayed({ startIntent() }, 3000)
    }

    private fun startIntent() {
        CommonUtility.Companion.commonStartActivity(this,LoginScreen::class.java,null,false)
    }
}