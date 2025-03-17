package com.example.hrms.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hrms.databinding.ActivitySalaryBreakdownBinding

class SalaryBreakdownActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySalaryBreakdownBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalaryBreakdownBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve salary values from Intent
        val finalSalary = intent.getStringExtra("final_salary") ?: "0"

        // Set salary values in UI
        binding.txtFinalSalary.text = "₹$finalSalary"

        // Back button functionality
        binding.imgLeaveStatusBack.setOnClickListener {
            finish()
        }
    }
}
