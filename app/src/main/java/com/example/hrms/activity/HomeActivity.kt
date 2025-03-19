package com.example.hrms.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
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
import com.example.hrms.RetrofitClient
import com.example.hrms.databinding.ActivityHomeBinding
import com.example.hrms.preferences.PreferenceManager
import com.example.hrms.responses.DashboardResponse
import com.example.hrms.responses.EnterAttendanceResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var isPunchIn = false
    private var punchInTime: Long = 0
    private val handler = Handler(Looper.getMainLooper())

    private var action = "punch_in"

    private var longitude: Double = -1.0
    private var latitude: Double = -1.0

    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            updateTimerText()
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        preferenceManager = PreferenceManager(this@HomeActivity)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        updateDate()
        checkLocationPermission()
        listeners()
        dashboard()
    }

    private fun listeners() {
        binding.btnPunch.setOnClickListener {
            punchStatus()
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
        binding.employeeListCard.setOnClickListener {
            startActivity(Intent(this, DepartmentActivity::class.java))
        }
        binding.publicHolidaysCard.setOnClickListener {
            startActivity(Intent(this, HolidayActivity::class.java))
        }
    }

    private fun punchStatus() {
        action = if (!isPunchIn) "punch_in" else "punch_out"
        insertAttendance()
    }

    private fun updateTimerText() {
        if (punchInTime == 0L) return
        val elapsedTime = (System.currentTimeMillis() - punchInTime) / 1000
        val hours = elapsedTime / 3600
        val minutes = (elapsedTime % 3600) / 60
        val seconds = elapsedTime % 60
        binding.tvTimer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun updateDate() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.tvDate.text = dateFormat.format(Date())
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        } else {
            getLastLocation()
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
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
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
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
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<EnterAttendanceResponse> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Toast.makeText(this@HomeActivity, "Error", Toast.LENGTH_SHORT).show()
                }

                override fun onComplete() {}

                override fun onNext(t: EnterAttendanceResponse) {
                    if (action == "punch_in" && t.message == "Punch-in recorded successfully!") {
                        punchInTime = System.currentTimeMillis()
                        isPunchIn = true
                        binding.btnPunch.text = "Punch Out"
                        handler.post(updateTimerRunnable)
                    } else if (action == "punch_out" && t.message == "Punch-out recorded successfully!") {
                        punchInTime = 0
                        isPunchIn = false
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

    private fun dashboard() {
        val userId = preferenceManager.getUserId()
        val apiService = RetrofitClient.getInstance()

        apiService.dashboard("select", userId.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<DashboardResponse> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Toast.makeText(this@HomeActivity, "Error", Toast.LENGTH_SHORT).show()
                }

                override fun onComplete() {}

                override fun onNext(t: DashboardResponse) {
                    if (t.message == "No attendance record found for today") {
                        isPunchIn = false
                        punchInTime = 0
                        binding.btnPunch.text = "Punch In"
                        binding.tvTimer.text = "00:00:00"
                        binding.progressCircular.progress = 0
                        handler.removeCallbacks(updateTimerRunnable)
                    } else {
                        val punchInTimeString = t.attendance?.get(0)?.a_punch_in_time

                        punchInTimeString?.let { timeStr ->
                            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                            sdf.timeZone = TimeZone.getDefault()

                            try {
                                val today = Calendar.getInstance()
                                val punchInCalendar = Calendar.getInstance()
                                val punchInDate = sdf.parse(timeStr)

                                punchInCalendar.time = punchInDate!!
                                punchInCalendar.set(Calendar.YEAR, today.get(Calendar.YEAR))
                                punchInCalendar.set(Calendar.MONTH, today.get(Calendar.MONTH))
                                punchInCalendar.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH))

                                punchInTime = punchInCalendar.timeInMillis
                                isPunchIn = true
                                binding.btnPunch.text = "Punch Out"
                                handler.post(updateTimerRunnable)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                }
            })
    }

}
