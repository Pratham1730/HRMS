package com.example.hrms.activity

import android.content.Intent
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

    private var isPunchIn = false
    private var secondsElapsed = 0
    private val handler = Handler(Looper.getMainLooper())

    private var punchInTime : Calendar?= null

    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            secondsElapsed++
            updateTimerText()
            updateProgress()
            handler.postDelayed(this, 1000) // Update every second
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateDate()

        binding.btnPunch.setOnClickListener {
            togglePunchStatus()
        }

        binding.leaveCard.setOnClickListener {
            val intent = Intent(this, LeaveStatusActivity::class.java)
            startActivity(intent)
        }


        binding.profileimage.setOnClickListener {
            val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun togglePunchStatus() {
        isPunchIn = !isPunchIn

        if (isPunchIn) {
            punchInTime=Calendar.getInstance()
            displayPunchInTime()
            binding.btnPunch.text = "Punch Out"
            secondsElapsed = 0
            handler.post(updateTimerRunnable)
        } else {
            binding.btnPunch.text = "Punch In"
            handler.removeCallbacks(updateTimerRunnable)
            resetTimer()
        }
    }

    private fun resetTimer() {
        secondsElapsed = 0
        updateTimerText()
        updateProgress()
    }

    private fun updateTimerText() {
        val hours = secondsElapsed / 3600
        val minutes = (secondsElapsed % 3600) / 60
        val seconds = secondsElapsed % 60
        binding.tvTimer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun updateProgress() {
        val maxTime = 8 * 3600
        val progress = (secondsElapsed.toFloat() / maxTime) * 100
        binding.progressCircular.progress = progress.toInt()
    }

    private fun updateDate() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        binding.tvDate.text = currentDate
    }

    private fun displayPunchInTime() {
        val timeFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        Toast.makeText(this@HomeActivity, timeFormat.format(punchInTime!!.time), Toast.LENGTH_SHORT)
            .show()

    }
}
