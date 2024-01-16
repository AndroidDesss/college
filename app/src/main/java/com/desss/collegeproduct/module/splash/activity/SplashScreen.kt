package com.desss.collegeproduct.module.splash.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.ActivitySplashScreenBinding
import com.desss.collegeproduct.module.splash.SplashScreenViewModel

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private lateinit var splashScreenBinding: ActivitySplashScreenBinding
    private lateinit var splashScreenViewModel: SplashScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        splashScreenViewModel = SplashScreenViewModel(application ,this)
//        splashScreenBinding.title.text=""    call like this for xml ids
    }
}