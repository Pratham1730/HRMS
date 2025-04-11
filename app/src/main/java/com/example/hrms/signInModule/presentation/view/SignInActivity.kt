package com.example.hrms.signInModule.presentation.view

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.example.hrms.responses.LoginResponse
import com.example.hrms.RetrofitClient
import com.example.hrms.activity.ForgetPasswordActivity
import com.example.hrms.activity.HomeActivity
import com.example.hrms.databinding.ActivitySignInBinding
import com.example.hrms.preferences.PreferenceManager
import com.example.hrms.signInModule.presentation.viewModel.LoginViewModel
import com.example.hrms.common.ApiResultState
import com.example.hrms.signUpModule.presentation.view.SelectCompanyActivity
import com.example.hrms.signUpModule.presentation.viewModel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private val viewModel : LoginViewModel by viewModels()

    private lateinit var binding: ActivitySignInBinding
    private lateinit var preferenceManager: PreferenceManager
    private var email: String = ""
    private var userId: Int = -1
    private var companyId: Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        preferenceManager = PreferenceManager(this@SignInActivity)

        if (preferenceManager.getIsPrevSignIn() == true){
            startActivity(Intent(this@SignInActivity , HomeActivity::class.java))
            finish()
        }

        observeUser()

        listeners()

    }

    private fun listeners() {
        binding.txtForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
        binding.txtMoveToSignUp.setOnClickListener {
            binding.edtSignInEmail.text?.clear()
            binding.edtSignInPassword.text?.clear()
            val intent = Intent(this@SignInActivity, SelectCompanyActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            callSignIn()
        }
    }

    private fun callSignIn(){

        email = binding.edtSignInEmail.text?.trim().toString()
        val password = binding.edtSignInPassword.text?.trim().toString()
        val method = "method"

        viewModel.loginUser(method , email , password)
    }

    private fun observeUser() {
        viewModel.login.observe(this) { result  ->
            when (result ) {
                is ApiResultState.Loading -> {
                    // Optional: Show loading UI
                }
                is ApiResultState.Success -> {
                    userId = result.data.user?.uId!!
                    companyId = result.data.user.companyId!!
                    email = result.data.user.uEmail!!
                    moveToMainPage()
                }
                is ApiResultState.ApiError -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }

                is ApiResultState.ServerError -> {

                }
            }
        }
    }


//    private fun callSignIn() {
//        val apiService = RetrofitClient.getInstance()
//
//        email = binding.edtSignInEmail.text?.trim().toString()
//        val password = binding.edtSignInPassword.text?.trim().toString()
//        val method = "method"
//
//        apiService.setLogin(method, email, password)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<LoginResponse> {
//                override fun onSubscribe(d: Disposable) {
//                }
//
//                override fun onError(e: Throwable) {}
//
//                override fun onComplete() {}
//
//                override fun onNext(t: LoginResponse) {
//                    Toast.makeText(this@SignInActivity, t.message.toString(), Toast.LENGTH_SHORT)
//                        .show()
//                    if (t.status!!.toInt() == 200) {
//                        userId = t.user?.u_id!!.toInt()
//                        companyId = t.user.company_id!!.toInt()
//                        moveToMainPage()
//                    }
//                }
//
//            })
//    }

    fun moveToMainPage() {
        preferenceManager.saveCompanyId(companyId)
        preferenceManager.isPrevSignIn(true)
        preferenceManager.saveUserEmail(email)
        preferenceManager.saveUserId(userId)
        binding.edtSignInEmail.text?.clear()
        binding.edtSignInPassword.text?.clear()
        startActivity(Intent(this@SignInActivity, HomeActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}