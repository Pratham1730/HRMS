package com.example.hrms.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hrms.R
import com.example.hrms.databinding.ActivityDepartmentBinding

class DepartmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDepartmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDepartmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDepartmentBack.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }

    }
}