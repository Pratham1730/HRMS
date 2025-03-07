package com.example.hrms.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hrms.R
import com.example.hrms.RetrofitClient
import com.example.hrms.databinding.ActivityLeaveStatusBinding
import com.example.hrms.preferences.PreferenceManager
import com.example.hrms.responses.LeaveListResponse
import com.example.hrms.responses.LeaveRequestResponse
import com.example.hrms.responses.LeaveTypeResponse
import com.example.hrms.responses.LeaveTypesItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class LeaveStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeaveStatusBinding
    private  var baseUrl = "http://192.168.4.140/"
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var leavelist : List<LeaveListResponse?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaveStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this@LeaveStatusActivity)
        binding.fabAddLeave.setOnClickListener{
            val intent = Intent(this,LeaveActivity::class.java)
            startActivity(intent)
        }


    }

    private fun callLeaveType(){
        val apiService = RetrofitClient.getInstance(baseUrl)
        val userid = preferenceManager.getUserId()

        apiService.selectLeave("select_leave", userid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<LeaveListResponse> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {}

                override fun onComplete() {

                }

                override fun onNext(t: LeaveListResponse) {
//                 leavelist = t.leaveData!!
                    Toast.makeText(this@LeaveStatusActivity, t.message.toString(), Toast.LENGTH_SHORT).show()

                }

            })
    }


}