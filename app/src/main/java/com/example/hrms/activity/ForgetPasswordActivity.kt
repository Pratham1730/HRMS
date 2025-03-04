package com.example.hrms.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hrms.databinding.ActivityForgetPasswordBinding

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnForgotPasswordVerify.setOnClickListener {
            val email = binding.edtForgotPasswordEmail.text.toString().trim()
            val number = binding.edtForgotPasswordMobile.text.toString().trim()

            if (email.isEmpty() || number.isEmpty()) {
                Toast.makeText(this, "Please enter Email and Number", Toast.LENGTH_SHORT).show()
            }  else {
                Toast.makeText(this@ForgetPasswordActivity, "Invalid Email or Number", Toast.LENGTH_SHORT).show()
            }
        }

    }
}