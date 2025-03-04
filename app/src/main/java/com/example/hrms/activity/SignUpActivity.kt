package com.example.hrms.activity

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hrms.R
import com.example.hrms.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listeners()

    }

    private fun listeners(){
        binding.btnSignUp.setOnClickListener {
            validations()
        }
    }

    private fun validations(){
        if (binding.edtSignUpName.text.toString().isEmpty()){
            binding.edtSignUpName.error = "Name Not Entered"
            binding.edtSignUpName.requestFocus()
        }
        else if (binding.edtSignUpEmail.text.toString().isEmpty()){
            binding.edtSignUpEmail.error = "Last Name Not Entered"
            binding.edtSignUpEmail.requestFocus()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtSignUpEmail.text.toString()).matches()){
            binding.edtSignUpEmail.error = "Please Enter Valid Email"
            binding.edtSignUpEmail.requestFocus()
        }
//        else if (binding.edtSignUpDOB.text.toString().isEmpty()){
//            binding.edtSignUpDOB.error = "DOB Not Entered"
//            binding.edtSignUpDOB.requestFocus()
//        }
        else if (binding.edtSignUpPassword.text.toString().isEmpty()){
            binding.edtSignUpPassword.error = "Password Not Entered"
            binding.edtSignUpPassword.requestFocus()
        }
        else if (binding.edtSignUpConfirmPassword.text.toString().isEmpty()){
            binding.edtSignUpConfirmPassword.error = "Password Not Entered"
            binding.edtSignUpConfirmPassword.requestFocus()
        }
        else if(binding.edtSignUpPhoneNumber.text.toString().isEmpty()){
            binding.edtSignUpPhoneNumber.error = "Phone Number Not Entered"
            binding.edtSignUpPhoneNumber.requestFocus()
        }
        else{
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
        }
    }

}