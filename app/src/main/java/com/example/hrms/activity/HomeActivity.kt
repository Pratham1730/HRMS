package com.example.hrms.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hrms.databinding.ActivityHomeBinding
import com.example.hrms.preferences.PreferenceManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var preferenceManager: PreferenceManager

    private var isPunchIn = false
    private var punchInTime: Long = 0
    private val handler = Handler(Looper.getMainLooper())

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

        preferenceManager = PreferenceManager(this@HomeActivity)

        loadPunchStatus()
        updateDate()

        listeners()

    }

    private fun listeners(){
        binding.btnPunch.setOnClickListener {
            togglePunchStatus()
        }

        binding.leaveCard.setOnClickListener {
            startActivity(Intent(this, LeaveStatusActivity::class.java))
        }

        binding.profileimage.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.attendanceCard.setOnClickListener {
            startActivity(Intent(this, AttendanceActivity::class.java))
        }

        binding.departmentCard.setOnClickListener {
            startActivity(Intent(this, DepartmentActivity::class.java))
        }
    }


//    private fun togglePunchStatus() {
//        isPunchIn = !isPunchIn
//
//        if (isPunchIn) {
//            punchInTime = System.currentTimeMillis()
//            sharedPreferences.edit().putLong("PUNCH_IN_TIME", punchInTime).apply()
//            sharedPreferences.edit().putBoolean("IS_PUNCHED_IN", true).apply()
//            binding.btnPunch.text = "Punch Out"
//            handler.post(updateTimerRunnable)
//        } else {
//            Toast.makeText(this@HomeActivity, binding.tvTimer.text.toString(), Toast.LENGTH_SHORT).show()
//            sharedPreferences.edit().putBoolean("IS_PUNCHED_IN", false).apply()
//            binding.btnPunch.text = "Punch In"
//            handler.removeCallbacks(updateTimerRunnable)
//        }
//    }


    private fun togglePunchStatus() {
        isPunchIn = !isPunchIn

        if (isPunchIn) {
            punchInTime = System.currentTimeMillis()
            Toast.makeText(this@HomeActivity,punchInTime.toString(), Toast.LENGTH_SHORT).show()
            preferenceManager.savePunchInTime(punchInTime)
            preferenceManager.isPunchIn(true)
            binding.btnPunch.text = "Punch Out"
            handler.post(updateTimerRunnable)
        } else {
            Toast.makeText(this@HomeActivity, binding.tvTimer.text.toString(), Toast.LENGTH_SHORT).show()

            punchInTime = 0
            preferenceManager.removePunchInTime()
            preferenceManager.isPunchIn(false)
            binding.btnPunch.text = "Punch In"
            binding.tvTimer.text = "00:00:00"
            binding.progressCircular.progress = 0

            handler.removeCallbacks(updateTimerRunnable)
        }
    }


    private fun loadPunchStatus() {
        isPunchIn = preferenceManager.getIsPunchIn()
        punchInTime = preferenceManager.getPunchInTime()

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
        binding.tvDate.text = dateFormat.format(Date())
    }
}
