package com.example.hrms.signUpModule.presentation.view

import android.app.DatePickerDialog
import com.example.hrms.adapter.CustomSpinnerAdapter
import android.content.Intent
import android.content.pm.ActivityInfo
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hrms.databinding.ActivitySignUpBinding
import com.example.hrms.signInModule.presentation.view.SignInActivity
import com.example.hrms.common.ApiResultState
import com.example.hrms.signUpModule.domain.model.DepartmentsDomainItem
import com.example.hrms.signUpModule.domain.model.PositionsDomainItem
import com.example.hrms.signUpModule.presentation.viewModel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.regex.Pattern

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private val viewModel : SignUpViewModel by viewModels()

    private var department: String = "Department"
    private var position: String = "Position"
    private var gender: String = "Gender"
    private var departmentId = -1
    private var genderId = -1
    private var positionId = -1
    private var companyId = 0
    private var selectedCalendarDOB: Calendar = Calendar.getInstance()
    private var selectedCalendarJoiningDate: Calendar = Calendar.getInstance()

    private lateinit var departmentList: List<DepartmentsDomainItem?>
    private lateinit var positionList: List<PositionsDomainItem?>

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        listeners()

        companyId = intent.getIntExtra("COMPANY_ID" , 0)

        getDept()
        observePositionList()
        observeDeptList()
        observeSignUp()
        genderSpinner()

    }

    private fun listeners() {
        binding.btnSignUp.setOnClickListener {
            validation()
        }
        binding.txtMoveToSignInPage.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
            finish()
        }
        binding.edtSignUpDOB.setOnClickListener {
            onClickDateDOB()
        }
        binding.edtSignUpJoiningDate.setOnClickListener {
            onClickDateJoining()
        }
        binding.imgSignUpBack.setOnClickListener {
            finish()
        }
    }


    private fun validation() {
        val password = binding.edtSignUpPassword.text.toString()
        val confirmPassword = binding.edtSignUpConfirmPassword.text.toString()

        if (binding.edtSignUpName.text.toString().isEmpty()) {
            binding.edtSignUpName.requestFocus()
            binding.edtSignUpName.error = "Please add full name"
        } else if (binding.edtSignUpEmail.text.toString().isEmpty()) {
            binding.edtSignUpEmail.requestFocus()
            binding.edtSignUpEmail.error = "Please add email"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtSignUpEmail.text.toString())
                .matches()
        ) {
            Toast.makeText(this, "Email not verified", Toast.LENGTH_SHORT).show()
        } else if (binding.edtSignUpPhoneNumber.text.toString().isEmpty()) {
            binding.edtSignUpPhoneNumber.requestFocus()
            binding.edtSignUpPhoneNumber.error = "Please add phone number"
        }
        else if(binding.edtSignUpPhoneNumber.text.toString().length < 10){
            binding.edtSignUpPhoneNumber.requestFocus()
            binding.edtSignUpPhoneNumber.error = "Phone number can't be less tha 10 digits"
        }
        else if (binding.edtSignUpDOB.text.toString().isEmpty()) {
            binding.edtSignUpDOB.requestFocus()
            binding.edtSignUpDOB.error = "Please add birth date"
        } else if (binding.edtSignUpJoiningDate.text.toString().isEmpty()) {
            binding.edtSignUpJoiningDate.requestFocus()
            binding.edtSignUpJoiningDate.error = "Please add joining date"
        } else if (binding.edtSignUpPassword.text.toString().isEmpty()) {
            binding.edtSignUpPassword.requestFocus()
            binding.edtSignUpPassword.error = "Please add password"
        } else if (!isValidPassword(password = password)) {
            binding.edtSignUpPassword.requestFocus()
            binding.edtSignUpPassword.error =
                "Password must have at least 8 characters, 1 uppercase, 1 lowercase, 1 digit, and 1 special character"
        } else if (binding.edtSignUpConfirmPassword.text.toString().isEmpty()) {
            binding.edtSignUpConfirmPassword.requestFocus()
            binding.edtSignUpConfirmPassword.error = "Confirm your password"
        } else if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            binding.edtSignUpConfirmPassword.requestFocus()
            binding.edtSignUpConfirmPassword.error = "Passwords do not match"
        } else if (genderId == -1) {
            Toast.makeText(this@SignUpActivity, "Enter Correct Gender", Toast.LENGTH_SHORT)
                .show()
        } else if (departmentId == -1) {
            Toast.makeText(
                this@SignUpActivity,
                "Enter Correct Department",
                Toast.LENGTH_SHORT
            ).show()
        } else if (positionId == -1) {
            Toast.makeText(this@SignUpActivity, "Enter Correct Position", Toast.LENGTH_SHORT).show()
        } else {
            signUpUser()
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+$).{8,}$"
        )
        return passwordPattern.matcher(password).matches()
    }


    private fun genderSpinner() {
        val genderList = arrayOf("Gender", "Male", "Female")
        val adapter = CustomSpinnerAdapter(this, genderList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderSpinner.adapter = adapter

        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                gender = genderList[p2]
                if (gender == "Male") {
                    genderId = 1
                } else if (gender == "Female") {
                    genderId = 2
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }


    private fun getDept(){
        viewModel.loadDept("select_dept" , companyId)
    }

    fun getPosition(){
        viewModel.loadPositions("select" , departmentId)
    }

    private fun signUpUser(){
        val insert = "insert"
        val name = binding.edtSignUpName.text.toString().trim()
        val email = binding.edtSignUpEmail.text.toString().trim()
        val password = binding.edtSignUpPassword.text.toString().trim()
        val phone = binding.edtSignUpPhoneNumber.text.toString().trim().toBigInteger()
        val deptId = departmentId
        val positionId = positionId
        val salary = 20000

        val joiningString = binding.edtSignUpJoiningDate.text.toString().trim()
        val joiningFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val joiningDateF = joiningFormat.parse(joiningString)
        val formatedJoining = if (joiningDateF != null) joiningFormat.format(joiningDateF) else ""

        val dobString = binding.edtSignUpDOB.text.toString().trim()
        val dobFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dobDate = dobFormat.parse(dobString)
        val formatedDob = if (dobDate != null) dobFormat.format(dobDate) else ""


        viewModel.signUpUser(insert , name , phone ,email , deptId , genderId , positionId , salary , formatedDob , formatedJoining , password , companyId)
    }


    private fun observeDeptList() {
        viewModel.deptList.observe(this) { result  ->
            when (result ) {
                is ApiResultState.Loading -> {
                    // Optional: Show loading UI
                }
                is ApiResultState.Success -> {
                    viewModel.departments.observe(this){ deptItem ->
                        departmentList = deptItem
                        departmentSpinner()
                    }
                }
                is ApiResultState.ApiError -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }

                is ApiResultState.ServerError -> {

                }
            }
        }
    }

    private fun observePositionList(){
        viewModel.position.observe(this){result ->
            when (result ) {
                is ApiResultState.Loading -> {
                    // Optional: Show loading UI
                }
                is ApiResultState.Success -> {
                    viewModel.positionsList.observe(this){ positionItem ->
                        positionList = positionItem
                        positionSpinner()
                    }
                }
                is ApiResultState.ApiError -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }

                is ApiResultState.ServerError -> {

                }
            }
        }
    }

    private fun observeSignUp(){
        viewModel.signUpUser.observe(this){result ->
            when (result ) {
                is ApiResultState.Loading -> {
                    // Optional: Show loading UI
                }
                is ApiResultState.Success -> {
                    Toast.makeText(this, "Sign Up Success", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is ApiResultState.ApiError -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }

                is ApiResultState.ServerError -> {

                }
            }
        }
    }



    private fun departmentSpinner() {
        if (departmentList.isEmpty()) return

        val departmentArray = Array(departmentList.size + 1) { "" }
        departmentArray[0] = "Department"  // Default value

        for (i in departmentList.indices) {
            departmentArray[i + 1] = departmentList[i]?.deptName ?: "Unknown"
        }

        val adapter = CustomSpinnerAdapter(this, departmentArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.departmentSpinner.adapter = adapter

        binding.departmentSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        department = departmentArray[position]
                        departmentId =
                            departmentList[position - 1]?.deptId.toString().toInt()
                        getPosition()
                    } else {
                        departmentId = -1
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }


    private fun positionSpinner() {
//        if (positionList.isEmpty()) {
//            return
//        }

        val positionArray = Array(positionList.size + 1) { "" }
        positionArray[0] = "Position"  // Default value

        for (i in positionList.indices) {
            positionArray[i + 1] = positionList[i]?.positionName ?: "Unknown"
        }
        val adapter = CustomSpinnerAdapter(this, positionArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.positionSpinner.adapter = adapter

        binding.positionSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p2 > 0) {
                        position = positionArray[p2]
                        positionId =
                            positionList[p2 - 1]?.positionId.toString().toInt()
                    } else {
                        positionId = -1
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }


    private fun onClickDateDOB() {
        val c = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            selectedCalendarDOB.set(Calendar.YEAR, year)
            selectedCalendarDOB.set(Calendar.MONTH, month)
            selectedCalendarDOB.set(Calendar.DAY_OF_MONTH, day)

            val tDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedCalendarDOB.time)
            binding.edtSignUpDOB.setText(tDate)
        }

        val datePickerDialog = DatePickerDialog(
            this, dateSetListener,
            c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.maxDate = c.timeInMillis

        datePickerDialog.show()

    }

    private fun onClickDateJoining() {
        val c = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            selectedCalendarJoiningDate.set(Calendar.YEAR, year)
            selectedCalendarJoiningDate.set(Calendar.MONTH, month)
            selectedCalendarJoiningDate.set(Calendar.DAY_OF_MONTH, day)

            val tDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                selectedCalendarJoiningDate.time
            )
            binding.edtSignUpJoiningDate.setText(tDate)
        }

        DatePickerDialog(
            this, dateSetListener,
            c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }


//    private fun callPosition() {
//        if (departmentId == -1) return
//        val apiService = RetrofitClient.getInstance()
//
//        apiService.getPosition("select", departmentId)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<PositionResponse> {
//                override fun onSubscribe(d: Disposable) {}
//
//                override fun onError(e: Throwable) {
//                    Toast.makeText(this@SignUpActivity, "Error", Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onComplete() {}
//
//                override fun onNext(response: PositionResponse) {
//                    positionList = response.positions
//                    positionSpinner()
//                }
//            })
//    }


//    private fun signUpUser() {
//        val insert = "insert"
//        val name = binding.edtSignUpName.text.toString().trim()
//        val email = binding.edtSignUpEmail.text.toString().trim()
//        val password = binding.edtSignUpPassword.text.toString().trim()
//        val phone = binding.edtSignUpPhoneNumber.text.toString().trim().toBigInteger()
//        val deptId = departmentId
//        val positionId = positionId
//        val salary = 20000
//
//        val joiningString = binding.edtSignUpJoiningDate.text.toString().trim()
//        val joiningFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//        val joiningDateF = joiningFormat.parse(joiningString)
//        val formatedJoining = if (joiningDateF != null) joiningFormat.format(joiningDateF) else ""
//
//        val dobString = binding.edtSignUpDOB.text.toString().trim()
//        val dobFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//        val dobDate = dobFormat.parse(dobString)
//        val formatedDob = if (dobDate != null) dobFormat.format(dobDate) else ""
//
//        val apiService = RetrofitClient.getInstance()
//
//        apiService.signUpUser(
//            insert,
//            name,
//            email,
//            password,
//            phone,
//            genderId,
//            deptId,
//            positionId,
//            salary,
//            formatedJoining,
//            formatedDob,
//            companyId
//        )
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<ApiResponse> {
//                override fun onSubscribe(d: Disposable) {
//                }
//
//                override fun onError(e: Throwable) {
//                }
//
//                override fun onComplete() {
//                }
//
//                override fun onNext(t: ApiResponse) {
//                    if (t.status == 200){
//                        finish()
//                    }
//                }
//            })
//    }


}

