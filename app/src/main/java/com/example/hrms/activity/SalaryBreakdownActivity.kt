package com.example.hrms.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hrms.databinding.ActivitySalaryBreakdownBinding
import com.example.hrms.models.SalaryModel

class SalaryBreakdownActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySalaryBreakdownBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalaryBreakdownBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val model = intent.getSerializableExtra("Salary") as SalaryModel

        binding.txtFinalSalary.text = model.finalSalary
        binding.txtOriginalSalary.text = model.originalSalary
        binding.txtDeduction.text = model.totalDeduction

        binding.imgLeaveStatusBack.setOnClickListener {
            finish()
        }
    }
}
