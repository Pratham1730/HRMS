package com.example.hrms.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hrms.adapter.MonthRVAdapter
import com.example.hrms.databinding.ActivityAttendanceBinding

class AttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAttendanceBinding
    private lateinit var adapter: MonthRVAdapter
    private val monthList = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()

//
//        binding.attendanceback.setOnClickListener {
//            startActivity(Intent(this,HomeActivity::class.java))
//        }
    }

    private fun setupRecyclerView() {
        binding.recyclerAttendance.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adapter = MonthRVAdapter(this,monthList)
        binding.recyclerAttendance.adapter = adapter
    }

    private fun setupListeners() {
        binding.attendanceback.setOnClickListener {
            finish()
        }
    }
}
