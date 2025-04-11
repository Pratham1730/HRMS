package com.example.hrms.leaveModule.applyLeaveModule.presentation.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.hrms.RetrofitClient
import com.example.hrms.activity.HomeActivity
import com.example.hrms.activity.LeaveStatusActivity
import com.example.hrms.adapter.CustomSpinnerAdapter
import com.example.hrms.common.ApiResultState
import com.example.hrms.databinding.ActivityLeaveBinding
import com.example.hrms.preferences.PreferenceManager
import com.example.hrms.responses.LeaveRequestResponse
import com.example.hrms.leaveModule.applyLeaveModule.data.model.LeaveTypeResponse
import com.example.hrms.leaveModule.applyLeaveModule.data.model.LeaveTypesItem
import com.example.hrms.leaveModule.applyLeaveModule.domain.model.response.LeaveTypesDomainItem
import com.example.hrms.leaveModule.applyLeaveModule.presentation.viewModels.LeaveViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class LeaveActivity : AppCompatActivity() {

    private val viewModel : LeaveViewModel by viewModels()

    private lateinit var binding: ActivityLeaveBinding
    private val calendar = Calendar.getInstance()
    private var leaveType = ""
    private var leaveTypeId = 0
    private lateinit var preferenceManager: PreferenceManager
    private var weekend = false
    private lateinit var leaveList : List<LeaveTypesDomainItem?>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        binding.imgLeaveBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        preferenceManager = PreferenceManager(this@LeaveActivity)

        callLeaveType()
        observeLeaveType()

        listeners()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun listeners(){
        binding.imgLeaveBack.setOnClickListener {
            finish()
        }
        binding.etFromDate.setOnClickListener {
            showDatePicker()
        }
        binding.btnSubmit.setOnClickListener {
            validations()
        }
    }

    private fun validations(){
        if (leaveTypeId == 0){
            Toast.makeText(this@LeaveActivity, "Enter Correct Leave Type", Toast.LENGTH_SHORT).show()
        }
        else if (binding.etFromDate.text.toString().isEmpty()){
            binding.etFromDate.requestFocus()
            binding.etFromDate.error = "Please add date"
        }
        else if(binding.etReason.text.toString().isEmpty()){
            binding.etReason.requestFocus()
            binding.etReason.error = "Please add reason"
        }
        else{
            callApplyLeave()
        }
    }

    fun callLeaveType(){
        viewModel.loadLeaveTypes("select_leave")
    }

    fun observeLeaveType(){
        viewModel.leaveType.observe(this){result ->
            when (result ) {
                is ApiResultState.Loading -> {
                    // Optional: Show loading UI
                }
                is ApiResultState.Success -> {
                    viewModel.leaveTypeList.observe(this){ leaveItem ->
                        leaveList = leaveItem
                        leaveSpinner()
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


    private fun leaveSpinner(){

        if (leaveList.isEmpty()) return

        val leaveArray = Array(leaveList.size + 1) { "" }
        leaveArray[0] = "Leave Type"

        for (i in leaveList.indices) {
            leaveArray[i + 1] = leaveList[i]?.typeName ?: "Unknown"
        }

        val adapter = CustomSpinnerAdapter(this, leaveArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerLeaveType.adapter = adapter

        binding.spinnerLeaveType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 > 0) {
                    leaveType = leaveArray[p2]
                    leaveTypeId =
                        leaveList[p2 - 1]?.id.toString().toInt()
                }
                else{
                    leaveTypeId = 0
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

//    private fun callLeaveType(){
//        val apiService = RetrofitClient.getInstance()
//
//        apiService.leaveType("select_leave")
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<LeaveTypeResponse>{
//                override fun onSubscribe(d: Disposable) {
//                }
//
//                override fun onError(e: Throwable) {
//                }
//
//                override fun onComplete() {
//                }
//
//                override fun onNext(t: LeaveTypeResponse) {
//                    leaveList = t.leave_types!!
//                    leaveSpinner()
//                }
//            })
//    }

    private fun callApplyLeave(){
        val date = binding.etFromDate.text.toString()
        val reason = binding.etReason.text.toString()
        val userId = preferenceManager.getUserId()
        val companyId = preferenceManager.getCompanyId()

        val apiService = RetrofitClient.getInstance()
        apiService.applyLeave("true" , companyId , userId , leaveTypeId , reason , date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<LeaveRequestResponse>{
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }

                override fun onNext(t: LeaveRequestResponse) {
                    Toast.makeText(this@LeaveActivity, t.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LeaveActivity , LeaveStatusActivity::class.java))
                    finish()
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.etFromDate.setText(dateFormat.format(selectedDate.time))

                weekend = isWeekend(year, month + 1, dayOfMonth)

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.minDate = calendar.timeInMillis
        datePicker.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isWeekend(year: Int, month: Int, day: Int): Boolean {
        val date = LocalDate.of(year, month, day)
        return date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY
    }
}
