package com.example.hrms.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.hrms.responses.LoginResponse
import com.example.hrms.RetrofitClient
import com.example.hrms.databinding.ActivitySignInBinding
import com.example.hrms.preferences.PreferenceManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val baseUrl = "http://192.168.4.140/"
    private lateinit var preferenceManager: PreferenceManager
    private var message: String = ""
    private var email: String = ""
    private var userId: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        preferenceManager = PreferenceManager(this@SignInActivity)

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
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            callSignIn()
        }
    }


    private fun callSignIn() {
        val apiService = RetrofitClient.getInstance(baseUrl)

        email = binding.edtSignInEmail.text?.trim().toString()
        val password = binding.edtSignInPassword.text?.trim().toString()
        val method = "method"

        apiService.setLogin(method, email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<LoginResponse> {
                override fun onSubscribe(d: Disposable) {
                    Toast.makeText(this@SignInActivity, "Subscribe", Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: Throwable) {}

                override fun onComplete() {}

                override fun onNext(t: LoginResponse) {
                    message = t.message.toString()
                    Toast.makeText(this@SignInActivity, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                    if (message == "Login successful") {
                        userId = t.user?.u_id!!.toInt()
                        moveToMainPage()
                    }
                }

            })
    }

    fun moveToMainPage() {
        preferenceManager.saveUserEmail(email)
        preferenceManager.saveUserId(userId)
        binding.edtSignInEmail.text?.clear()
        binding.edtSignInPassword.text?.clear()
        startActivity(Intent(this@SignInActivity, HomeActivity::class.java))
    }
}