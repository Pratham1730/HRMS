package com.example.hrms.activity

import android.app.DatePickerDialog
import com.example.hrms.adapter.CustomSpinnerAdapter
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hrms.databinding.ActivitySignUpBinding
import java.text.SimpleDateFormat
import java.util.Locale

class SignUpActivity : AppCompatActivity() {

    private var department: String = "Department"
    private var position: String = "Position"
    private var gender: String = "Gender"
    private var selectedCalendarDOB: Calendar = Calendar.getInstance()
    private var selectedCalendarJoiningDate: Calendar = Calendar.getInstance()


    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listeners()
        genderSpinner()
        positionSpinner()
        departmentSpinner()
    }

    private fun listeners() {
        binding.btnSignUp.setOnClickListener {
            validations()
        }
        binding.txtMoveToSignInPage.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
            finish()
        }
        binding.edtSignUpDOB.setOnClickListener {
            onClickDate()
        }
    }

    private fun validations() {
        if (binding.edtSignUpName.text.toString().isEmpty()) {
            binding.edtSignUpName.error = "Name Not Entered"
            binding.edtSignUpName.requestFocus()
        }
        else if (binding.edtSignUpEmail.text.toString().isEmpty()) {
            binding.edtSignUpEmail.error = "Email Not Entered"
            binding.edtSignUpEmail.requestFocus()
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.edtSignUpEmail.text.toString()).matches()) {
            binding.edtSignUpEmail.error = "Please Enter Valid Email"
            binding.edtSignUpEmail.requestFocus()
        }
        else if (binding.edtSignUpPassword.text.toString().isEmpty()) {
            binding.edtSignUpPassword.error = "Password Not Entered"
            binding.edtSignUpPassword.requestFocus()
        }
        else if (binding.edtSignUpConfirmPassword.text.toString().isEmpty()) {
            binding.edtSignUpConfirmPassword.error = "Confirm Password Not Entered"
            binding.edtSignUpConfirmPassword.requestFocus()
        }
        else if (binding.edtSignUpPhoneNumber.text.toString().isEmpty()) {
            binding.edtSignUpPhoneNumber.error = "Phone Number Not Entered"
            binding.edtSignUpPhoneNumber.requestFocus()
        }
        else if (binding.edtSignUpDOB.text.toString().isEmpty()){
            binding.edtSignUpDOB.error = "DOB Not Entered"
            binding.edtSignUpDOB.requestFocus()
        }
        else {
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
        }
    }

    private fun genderSpinner() {
        val genderList = arrayOf("Gender", "Male", "Female")
        val adapter = CustomSpinnerAdapter(this, genderList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderSpinner.adapter = adapter

        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                gender = genderList[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun positionSpinner() {
        val positionList = arrayOf("Position", "Intern", "Junior", "Senior")
        val adapter = CustomSpinnerAdapter(this, positionList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.positionSpinner.adapter = adapter

        binding.positionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                position = positionList[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun departmentSpinner() {
        val departmentList = arrayOf("Department", "Android", "PHP", "iOS")
        val adapter = CustomSpinnerAdapter(this, departmentList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.departmentSpinner.adapter = adapter

        binding.departmentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                department = departmentList[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun onClickDate(){
        val c = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            selectedCalendarDOB.set(Calendar.YEAR, year)
            selectedCalendarDOB.set(Calendar.MONTH, month)
            selectedCalendarDOB.set(Calendar.DAY_OF_MONTH, day)

            val tDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedCalendarDOB.time)
            binding.edtSignUpDOB.setText(tDate)
        }

        DatePickerDialog(
            this, dateSetListener,
            c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}

