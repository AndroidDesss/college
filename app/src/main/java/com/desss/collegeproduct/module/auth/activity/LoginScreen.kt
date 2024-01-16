package com.desss.collegeproduct.module.auth.activity

import androidx.lifecycle.Observer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.ActivityLoginScreenBinding
import com.desss.collegeproduct.module.auth.model.LoginModel
import com.desss.collegeproduct.module.auth.viewModel.LoginViewModel

class LoginScreen : AppCompatActivity() {

    private lateinit var loginScreenBinding: ActivityLoginScreenBinding
    private lateinit var loginScreenViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_login_screen)
        loginScreenViewModel = LoginViewModel(application, this)
        loginScreenBinding.btnSubmit.setOnClickListener {
            loginScreenViewModel.submitLogin(this, "read", "accounts_user", "know1", "Dess@123")
            observeViewModel(loginScreenViewModel)
        }
    }

    private fun observeViewModel(viewModel: LoginViewModel) {
        viewModel.submitLoginObservable()?.observe(this,
            Observer<Any?> { response ->
                if (response is LoginModel) {
                }
            })
    }
}

