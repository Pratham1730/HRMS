package com.example.hrms.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hrms.RetrofitClient
import com.example.hrms.adapter.MonthRVAdapter
import com.example.hrms.databinding.ActivityAttendanceBinding
import com.example.hrms.preferences.PreferenceManager
import com.example.hrms.responses.AttendanceResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.time.Year
import java.util.Calendar

class AttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAttendanceBinding
    private lateinit var adapter: MonthRVAdapter
    private var monthNumber = Calendar.getInstance().get(Calendar.MONTH) + 1
    private var year = Calendar.getInstance().get(Calendar.YEAR)
    private lateinit var preferenceManager: PreferenceManager

    private val monthList = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        preferenceManager = PreferenceManager(this@AttendanceActivity)

        setupRecyclerView()
        setupListeners()

        getAttendance()


        binding.btnSalaryBreakdown.setOnClickListener {
            if (monthNumber < Calendar.getInstance().get(Calendar.MONTH) + 1){
                val intent = Intent(this, SalaryBreakdownActivity::class.java)
                intent.putExtra("final_salary", binding.txtAttendanceSalary.text.toString())
                startActivity(intent)
            }
            else{
                Toast.makeText(this@AttendanceActivity, "You Can Only See The Salary Breakdown Of The Previous Months", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setupRecyclerView() {
        binding.recyclerAttendance.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adapter = MonthRVAdapter(this, monthList)
        binding.recyclerAttendance.adapter = adapter
        adapter.setUpInterface(object : MonthRVAdapter.OnMonthClickListener {
            override fun onClicked(position: Int) {
                monthNumber = position
                getAttendance()
            }
        })
    }

    private fun setupListeners() {
        binding.attendanceback.setOnClickListener {
            finish()
        }
    }

    private fun getAttendance() {
        var userId = preferenceManager.getUserId()
        val apiService = RetrofitClient.getInstance()
        apiService.getAttendance(userId, monthNumber, year)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<AttendanceResponse> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }

                override fun onNext(t: AttendanceResponse) {
                    if (t.status!!.toInt() == 200) {
                        binding.txtAttendancePresentDays.text = t.present_days.toString()
                        binding.txtAttendanceLeaves.text = t.paid_leave_days.toString()
                        binding.txtAttendanceHalfDays.text = t.half_days.toString()
                        binding.txtAttendanceUnpaidLeaves.text = t.unpaidLeaveDays.toString()
                        binding.txtAttendanceAbsentDays.text = t.absent_days.toString()
                        if (monthNumber < Calendar.getInstance().get(Calendar.MONTH) + 1){
                            binding.llSalary.visibility = View.VISIBLE
                            binding.txtAttendanceSalary.text = t.final_salary.toString()
                        }
                        else{
                            binding.llSalary.visibility = View.GONE
                        }
                    } else {
                        binding.txtAttendancePresentDays.text = "0"
                        binding.txtAttendanceLeaves.text = "0"
                        binding.txtAttendanceAbsentDays.text = "0"
                        binding.txtAttendanceUnpaidLeaves.text = "0"
                        binding.txtAttendanceHalfDays.text = "0"
                        if (monthNumber < Calendar.getInstance().get(Calendar.MONTH) + 1){
                            binding.llSalary.visibility = View.VISIBLE
                            binding.txtAttendanceSalary.text = "0"
                        }
                        else{
                            binding.llSalary.visibility = View.GONE
                        }
                    }
                }
            })
    }
}
