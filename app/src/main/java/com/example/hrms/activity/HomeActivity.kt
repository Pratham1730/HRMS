package com.example.hrms.activity

import android.content.Intent
import android.content.SharedPreferences
import android.Manifest
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.hrms.MyReachability
import com.example.hrms.RetrofitClient
import com.example.hrms.databinding.ActivityHomeBinding
import com.example.hrms.preferences.PreferenceManager
import com.example.hrms.responses.EnterAttendanceResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var isServerConnected = true

    private var isPunchIn = false
    private var punchInTime: Long = 0
    private val handler = Handler(Looper.getMainLooper())

    private var action = "punch_in"


    private var longitude: Double = -1.0
    private var latitude: Double = -1.0

    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            updateTimerText()
            updateProgress()
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        preferenceManager = PreferenceManager(this@HomeActivity)

        isConnected()


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        loadPunchStatus()
        updateDate()

        checkLocationPermission()

        listeners()

    }

    private fun listeners() {
        binding.btnPunch.setOnClickListener {
            if (isServerConnected) {
                punchStatus()
            } else {
                Toast.makeText(this@HomeActivity, "Server Not Connected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.leaveCard.setOnClickListener {
            if (isServerConnected) {
                startActivity(Intent(this, LeaveStatusActivity::class.java))
            } else {
                Toast.makeText(this@HomeActivity, "Server Not Connected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.profileimage.setOnClickListener {
            if (isServerConnected) {
                startActivity(Intent(this, ProfileActivity::class.java))
            } else {
                Toast.makeText(this@HomeActivity, "Server Not Connected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.attendanceCard.setOnClickListener {
            if (isServerConnected) {
                startActivity(Intent(this, AttendanceActivity::class.java))
            } else {
                Toast.makeText(this@HomeActivity, "Server Not Connected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.employeeListCard.setOnClickListener {
            if (isServerConnected) {
                startActivity(Intent(this, DepartmentActivity::class.java))

            } else {
                Toast.makeText(this@HomeActivity, "Server Not Connected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.publicHolidaysCard.setOnClickListener {
            if (isServerConnected) {
                startActivity(Intent(this, HolidayActivity::class.java))
            } else {
                Toast.makeText(this@HomeActivity, "Server Not Connected", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun punchStatus() {
        isPunchIn = !isPunchIn
        action = if (isPunchIn) "punch_in" else "punch_out"
        insertAttendance()
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
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.tvDate.text = dateFormat.format(Date())
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        } else {
            getLastLocation()
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                latitude = it.latitude
                longitude = it.longitude
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation()
        }
    }

    private fun insertAttendance() {
        val userId = preferenceManager.getUserId()
        val companyId = preferenceManager.getCompanyId()

        val punchInTimeFormat = Calendar.getInstance().time
        val formattedTime =
            SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(punchInTimeFormat)

        val punchInDate = binding.tvDate.text

        val apiService = RetrofitClient.getInstance()

        apiService.insertAttendance(
            userId,
            punchInDate.toString(),
            formattedTime.toString(),
            companyId,
            action,
            latitude,
            longitude
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<EnterAttendanceResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@HomeActivity, "Error", Toast.LENGTH_SHORT).show()
                }

                override fun onComplete() {
                }

                override fun onNext(t: EnterAttendanceResponse) {
                    if (t.message == "Punch-in recorded successfully!") {
                        punchInTime = System.currentTimeMillis()
                        preferenceManager.savePunchInTime(punchInTime)
                        preferenceManager.isPunchIn(true)
                        binding.btnPunch.text = "Punch Out"
                        handler.post(updateTimerRunnable)

                    } else if (t.message == "Punch-out recorded successfully!") {
                        punchInTime = 0
                        preferenceManager.removePunchInTime()
                        preferenceManager.isPunchIn(false)
                        binding.btnPunch.text = "Punch In"
                        binding.tvTimer.text = "00:00:00"
                        binding.progressCircular.progress = 0
                        handler.removeCallbacks(updateTimerRunnable)

                    }
                    Toast.makeText(this@HomeActivity, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun isConnected() {
        lifecycleScope.launch {
            val isServerReachable = withContext(Dispatchers.IO) {
                MyReachability.hasServerConnected(this@HomeActivity)
            }
            isServerConnected = isServerReachable
        }
    }

}
