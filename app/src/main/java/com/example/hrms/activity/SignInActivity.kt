package com.example.hrms.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.hrms.Models.LoginResponse
import com.example.hrms.RetrofitClient
import com.example.hrms.databinding.ActivitySignInBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    val baseUrl = "http://192.168.4.140/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        listeners()


    }

    private fun listeners(){
        binding.txtForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
        binding.txtMoveToSignUp.setOnClickListener {

            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            //validations()
            callSignIn()
        }
    }

//    private fun validations(){
//        val email = binding.edtSignInEmail.text.toString().trim()
//        val password = binding.edtSignInPassword.text.toString().trim()
//
//        if (email.isEmpty() || password.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(binding.edtSignInEmail.text.toString()).matches()) {
//            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
//        }
//        else {
//            Toast.makeText(this@SignInActivity, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun callSignIn() {
        val apiService = RetrofitClient.getInstance(baseUrl)

        val email = binding.edtSignInEmail.text?.trim().toString()
        val password = binding.edtSignInPassword.text?.trim().toString()
        val method = "method"

        apiService.setLogin(method , email , password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object  : Observer<LoginResponse>{
                override fun onSubscribe(d: Disposable) {
                    Toast.makeText(this@SignInActivity, "Subscribe", Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: Throwable) {}

                override fun onComplete() {}

                override fun onNext(t: LoginResponse) {
                    Toast.makeText(this@SignInActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                }

            })
    }
}