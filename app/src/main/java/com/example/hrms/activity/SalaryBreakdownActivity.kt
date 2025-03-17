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

        val finalSalary = intent.getStringExtra("final_salary")
        val originalSalary = intent.getStringExtra("original_salary")
        val deductions = intent.getStringExtra("deductions")

        binding.txtFinalSalary.text = finalSalary
        binding.txtOriginalSalary.text = originalSalary
        binding.txtDeduction.text = deductions

        binding.imgLeaveStatusBack.setOnClickListener {
            finish()
        }
    }
}
