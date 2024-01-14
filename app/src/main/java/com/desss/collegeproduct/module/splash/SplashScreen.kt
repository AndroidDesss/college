package com.desss.collegeproduct.module.splash

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private lateinit var splashScreenBinding: ActivitySplashScreenBinding
    private lateinit var splashScreenViewModel: SplashScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        splashScreenViewModel = SplashScreenViewModel(this)
//        splashScreenBinding.title.text=""    call like this for xml ids
    }
}