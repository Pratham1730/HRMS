package com.example.hrms.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hrms.RetrofitClient
import com.example.hrms.adapter.LeaveStatusRvAdapter
import com.example.hrms.databinding.ActivityLeaveStatusBinding
import com.example.hrms.preferences.PreferenceManager
import com.example.hrms.responses.LeaveDataItem
import com.example.hrms.responses.LeaveDeteleResponse
import com.example.hrms.responses.LeaveListResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class LeaveStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeaveStatusBinding
    private var baseUrl = "http://192.168.4.140/"
    private lateinit var preferenceManager: PreferenceManager
    private var leaveList: List<LeaveDataItem?> = emptyList()
    private lateinit var adapter: LeaveStatusRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaveStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)

        binding.recyclerLeaveTracker.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = LeaveStatusRvAdapter(this, leaveList) { leaveItem ->
            deleteLeave(leaveItem)
        }
        binding.recyclerLeaveTracker.adapter = adapter

        //backPressed()

        callLeaveList()

        listeners()
    }

    private fun listeners(){
        binding.fabAddLeave.setOnClickListener {
            startActivity(Intent(this, LeaveActivity::class.java))
            finish()
        }

        binding.imgLeaveStatusBack.setOnClickListener {
            finish()
        }
    }

    private fun callLeaveList() {
        val apiService = RetrofitClient.getInstance(baseUrl)
        val userId = preferenceManager.getUserId()

        apiService.selectLeave("select_leave", userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<LeaveListResponse> {
                override fun onSubscribe(d: Disposable) {}

                override fun onNext(response: LeaveListResponse) {
                    val newList = response.leave_data ?: emptyList()
                    if (newList.isNotEmpty()) {
                        leaveList = newList
                        adapter.updateList(leaveList)
                    } else {
                        Toast.makeText(this@LeaveStatusActivity, "No Leave Record Found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@LeaveStatusActivity, "Error fetching data", Toast.LENGTH_SHORT).show()
                }

                override fun onComplete() {}
            })
    }

    private fun deleteLeave(leaveItem: LeaveDataItem) {
        val apiService = RetrofitClient.getInstance(baseUrl)
        val companyId = preferenceManager.getCompanyId()
        val userId = preferenceManager.getUserId()

        leaveItem.l_id?.let {
            apiService.deleteLeave("delete", it, userId, companyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<LeaveDeteleResponse> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(response: LeaveDeteleResponse) {
                        leaveList = leaveList.filter { it?.l_id != leaveItem.l_id }
                        adapter.updateList(leaveList)
                        callLeaveList()
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(this@LeaveStatusActivity, "Failed to delete leave", Toast.LENGTH_SHORT).show()
                    }

                    override fun onComplete() {}
                })
        }
    }

}
