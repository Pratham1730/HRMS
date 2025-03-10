package com.example.hrms.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hrms.databinding.ActivityHomeBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPreferences: SharedPreferences

    private var isPunchIn = false
    private var punchInTime: Long = 0
    private val handler = Handler(Looper.getMainLooper())

    private var punchInTime : Calendar?= null

    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            updateTimerText()
            updateProgress()
            handler.postDelayed(this, 1000) // Update every second
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("HRMS_APP", MODE_PRIVATE)

        loadPunchStatus()
        updateDate()

        binding.btnPunch.setOnClickListener {
            togglePunchStatus()
        }

        binding.leaveCard.setOnClickListener {
            val intent = Intent(this,LeaveStatusActivity::class.java)
            startActivity(intent)
        }

        binding.profileimage.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun togglePunchStatus() {
        isPunchIn = !isPunchIn

        if (isPunchIn) {
            binding.btnPunch.text = "Punch Out"
            handler.post(updateTimerRunnable)
        } else {
            sharedPreferences.edit().putBoolean("IS_PUNCHED_IN", false).apply()

            binding.btnPunch.text = "Punch In"
            handler.removeCallbacks(updateTimerRunnable)
        }
    }

    private fun loadPunchStatus() {
        isPunchIn = sharedPreferences.getBoolean("IS_PUNCHED_IN", false)
        punchInTime = sharedPreferences.getLong("PUNCH_IN_TIME", 0)

        if (isPunchIn && punchInTime != 0L) {
            binding.btnPunch.text = "Punch Out"
            handler.post(updateTimerRunnable)
        } else {
            binding.btnPunch.text = "Punch In"
        }
    }


    private fun updateTimerText() {
        if (punchInTime == 0L) return

        val elapsedTime = (System.currentTimeMillis() - punchInTime) / 1000
        val hours = elapsedTime / 3600
        val minutes = (elapsedTime % 3600) / 60
        val seconds = elapsedTime % 60

        binding.tvTimer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun updateProgress() {
        val maxTime = 8 * 3600
        val elapsedTime = (System.currentTimeMillis() - punchInTime) / 1000
        val progress = (elapsedTime.toFloat() / maxTime) * 100
        binding.progressCircular.progress = progress.toInt()
    }

    private fun updateDate() {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        binding.tvDate.text = currentDate
    }

    private fun displayPunchInTime() {
        val timeFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        Toast.makeText(this@HomeActivity, timeFormat.format(punchInTime!!.time), Toast.LENGTH_SHORT)
            .show()

    }
}
