package com.example.hrms.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hrms.R
import com.example.hrms.databinding.ActivityLeaveBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LeaveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeaveBinding
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgLeaveBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.etFromDate.setOnClickListener { showDatePicker() }

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.leave_types,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerLeaveType.adapter = adapter

        binding.spinnerLeaveType.setOnTouchListener { _, _ ->
            binding.spinnerLeaveType.performClick()
            false
        }

        binding.spinnerLeaveType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    val selectedLeaveType = parent?.getItemAtPosition(position).toString()
                    Toast.makeText(this@LeaveActivity, "Selected: $selectedLeaveType", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.etFromDate.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
}
