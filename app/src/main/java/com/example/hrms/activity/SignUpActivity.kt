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
import com.example.hrms.responses.ApiResponse
import com.example.hrms.responses.DepartmentModel
import com.example.hrms.responses.DepartmentsItem
import com.example.hrms.responses.PositionResponse
import com.example.hrms.responses.PositionsItem
import com.example.hrms.databinding.ActivitySignUpBinding
import com.example.hrms.RetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.Locale

class SignUpActivity : AppCompatActivity() {

    private var department: String = "Department"
    private var position: String = "Position"
    private var gender: String = "Gender"
    private var departmentId = -1
    private var genderId = -1
    private var positionId = -1
    private var selectedCalendarDOB: Calendar = Calendar.getInstance()
    private var selectedCalendarJoiningDate: Calendar = Calendar.getInstance()

    private val baseUrl = "http://192.168.4.140/"
    private lateinit var departmentList: List<DepartmentsItem?>
    private lateinit var positionList: List<PositionsItem?>

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listeners()

        callDept()
        genderSpinner()
//
//        binding.imgSignUpBack.setOnClickListener {
//            val intent = Intent(this,SignInActivity::class.java)
//            startActivity(intent)
//        }


    }

    private fun listeners() {
        binding.btnSignUp.setOnClickListener {
            if (binding.edtSignUpPassword.text.toString() == binding.edtSignUpConfirmPassword.text.toString()){
                if (genderId == -1){
                    Toast.makeText(this@SignUpActivity, "Enter Correct Gender", Toast.LENGTH_SHORT).show()
                }
                else if(departmentId == -1){
                    Toast.makeText(this@SignUpActivity, "Enter Correct Department", Toast.LENGTH_SHORT).show()
                }
                else{
                    signUpUser()
                }

            }
            else{
                Toast.makeText(this@SignUpActivity, "Password Doesn't Match", Toast.LENGTH_SHORT).show()
            }

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
    }


    private fun genderSpinner() {
        val genderList = arrayOf("Gender", "Male", "Female")
        val adapter = CustomSpinnerAdapter(this, genderList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderSpinner.adapter = adapter

        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                gender = genderList[p2]
                if (gender == "Male"){
                    genderId = 1
                }
                else if (gender == "Female"){
                    genderId = 2
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }


    private fun departmentSpinner() {
        if (departmentList.isEmpty()) return

        val departmentArray = Array(departmentList.size + 1) { "" }
        departmentArray[0] = "Department"  // Default value

        for (i in departmentList.indices) {
            departmentArray[i + 1] = departmentList[i]?.deptName ?: "Unknown"
        }

        val a = departmentArray
        val adapter = CustomSpinnerAdapter(this, departmentArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.departmentSpinner.adapter = adapter

        binding.departmentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {  // Ensure a department is selected
                    department = departmentArray[position]
                    departmentId =
                        departmentList[position - 1]?.deptId.toString().toInt()
                    callPosition()  // Fetch positions based on selected department
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }




    private fun positionSpinner() {
        if (positionList.isEmpty()) {
            return
        }

        val positionArray = Array(positionList.size + 1) { "" }
        positionArray[0] = "Position"  // Default value

        for (i in positionList.indices) {
            positionArray[i + 1] = positionList[i]?.positionName ?: "Unknown"
        }
        val a = positionArray
        val adapter = CustomSpinnerAdapter(this, positionArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.positionSpinner.adapter = adapter

        binding.positionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 > 0){
                    position = positionArray[p2]
                    positionId =
                        positionList[p2-1]?.positionId.toString().toInt()
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
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedCalendarDOB.time)
            binding.edtSignUpDOB.setText(tDate)
        }

        DatePickerDialog(
            this, dateSetListener,
            c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun onClickDateJoining() {
        val c = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            selectedCalendarJoiningDate.set(Calendar.YEAR, year)
            selectedCalendarJoiningDate.set(Calendar.MONTH, month)
            selectedCalendarJoiningDate.set(Calendar.DAY_OF_MONTH, day)

            val tDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                selectedCalendarJoiningDate.time
            )
            binding.edtSignUpJoiningDate.setText(tDate)
        }

        DatePickerDialog(
            this, dateSetListener,
            c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }


    private fun callDept() {
        val apiService = RetrofitClient.getInstance(baseUrl)

        apiService.setDept("select_dept")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<DepartmentModel> {
                override fun onSubscribe(d: Disposable) {
//                    Toast.makeText(this@SignUpActivity, "Subscribe", Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: Throwable) {
//                    Toast.makeText(this@SignUpActivity, "error", Toast.LENGTH_SHORT).show()
                }

                override fun onComplete() {
//                    Toast.makeText(this@SignUpActivity, "Subscribe", Toast.LENGTH_SHORT).show()
                }

                override fun onNext(t: DepartmentModel) {
                    departmentList = t.departments!!
//                    Toast.makeText(
//                        this@SignUpActivity,
//                        departmentList.get(0)?.deptName.toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    Toast.makeText(this@SignUpActivity, "Success", Toast.LENGTH_SHORT).show()
                    departmentSpinner()
                }
            })
    }




private fun callPosition() {
    if (departmentId == -1) return  // Prevent API call if no valid department is selected

    val apiService = RetrofitClient.getInstance(baseUrl)

    val a = departmentId
    apiService.getPosition("select", departmentId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Observer<PositionResponse> {
            override fun onSubscribe(d: Disposable) {}

            override fun onError(e: Throwable) {
                Toast.makeText(this@SignUpActivity, "Error", Toast.LENGTH_SHORT).show()
            }

            override fun onComplete() {}

            override fun onNext(response: PositionResponse) {
                positionList = response.positions!!
                positionSpinner()  // Refresh position dropdown after fetching data
            }
        })
}




    private fun signUpUser() {
        val insert = "insert"
        val name = binding.edtSignUpName.text.toString().trim()
        val email = binding.edtSignUpEmail.text.toString().trim()
        val password = binding.edtSignUpPassword.text.toString().trim()
        val phone = binding.edtSignUpPhoneNumber.text.toString().trim().toBigInteger()
        val deptId = departmentId
        val positionId = positionId
        val salary = 20000
        val joiningDate = binding.edtSignUpJoiningDate.text.toString().trim()
        val dob = binding.edtSignUpDOB.text.toString().trim()
        val companyId = 1

        val apiService = RetrofitClient.getInstance(baseUrl)

        apiService.signUpUser(insert , name , email , password , phone , genderId , deptId , positionId , salary , joiningDate , dob , companyId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ApiResponse>{
                override fun onSubscribe(d: Disposable) {
                    Toast.makeText(this@SignUpActivity, "Sub", Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@SignUpActivity, "Error", Toast.LENGTH_SHORT).show()
                }

                override fun onComplete() {
                    Toast.makeText(this@SignUpActivity, "error", Toast.LENGTH_SHORT).show()
                }

                override fun onNext(t: ApiResponse) {
                    //Toast.makeText(this@SignUpActivity, "Success", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@SignUpActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }


}

