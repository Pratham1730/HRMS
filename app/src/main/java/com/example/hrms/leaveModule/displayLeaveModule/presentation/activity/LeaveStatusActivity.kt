package com.example.hrms.leaveModule.displayLeaveModule.presentation.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hrms.RetrofitClient
import com.example.hrms.common.ApiResultState
import com.example.hrms.leaveModule.displayLeaveModule.presentation.adapter.LeaveStatusRvAdapter
import com.example.hrms.databinding.ActivityLeaveStatusBinding
import com.example.hrms.leaveModule.applyLeaveModule.presentation.activity.LeaveActivity
import com.example.hrms.preferences.PreferenceManager
import com.example.hrms.leaveModule.displayLeaveModule.data.model.LeaveDataItem
import com.example.hrms.leaveModule.displayLeaveModule.data.model.LeaveDeteleResponse
import com.example.hrms.leaveModule.displayLeaveModule.domain.model.response.LeaveDataDomainItem
import com.example.hrms.leaveModule.displayLeaveModule.presentation.viewModels.LeaveListViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

@AndroidEntryPoint
class LeaveStatusActivity : AppCompatActivity() {

    private val viewModel: LeaveListViewModel by viewModels()

    private lateinit var binding: ActivityLeaveStatusBinding
    private lateinit var preferenceManager: PreferenceManager
    private var leaveList: List<LeaveDataDomainItem?> = emptyList()
    private var status = -1
    private lateinit var adapter: LeaveStatusRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaveStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        preferenceManager = PreferenceManager(this)

        binding.recyclerLeaveTracker.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = LeaveStatusRvAdapter(this, leaveList) { leaveItem ->
            deleteLeave(leaveItem)
        }
        binding.recyclerLeaveTracker.adapter = adapter

        //backPressed()

        callLeaveList()
        observeLeaveList()

        listeners()
    }

    private fun listeners() {
        binding.fabAddLeave.setOnClickListener {
            startActivity(Intent(this, LeaveActivity::class.java))
            finish()
        }

        binding.imgLeaveStatusBack.setOnClickListener {
            finish()
        }
    }

    fun callLeaveList() {
        val userId = preferenceManager.getUserId()
        viewModel.loadLeaveList("select_leave", userId)
    }

    fun observeLeaveList() {
        viewModel.leaveListResponse.observe(this) { result ->
            when (result) {
                is ApiResultState.Loading -> {
                    // Optional: Show loading UI
                }

                is ApiResultState.Success -> {
                    viewModel.leaveListItems.observe(this) { leaveItem ->
                        val newList = leaveItem ?: emptyList()
                        if (newList.isNotEmpty()) {
                            leaveList = newList
                            adapter.updateList(leaveList)
                            status = result.data.status!!.toInt()
                            showLayout()
                        } else {
                            Toast.makeText(
                                this@LeaveStatusActivity,
                                result.data.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            status = -1
                            showLayout()
                        }
                    }
                }

                is ApiResultState.ApiError -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }

                is ApiResultState.ServerError -> {

                }
            }
        }
    }

    private fun deleteLeave(leaveItem: LeaveDataDomainItem){
        val companyId = preferenceManager.getCompanyId()
        val userId = preferenceManager.getUserId()

        leaveItem.leaveId?.let{
            viewModel.deleteLeave("delete", it, userId, companyId)
        }
        callLeaveList()
        observeLeaveList()
    }
//    private fun callLeaveList() {
//        val apiService = RetrofitClient.getInstance()
//        val userId = preferenceManager.getUserId()
//
//        apiService.selectLeave("select_leave", userId)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<LeaveListResponse> {
//                override fun onSubscribe(d: Disposable) {}
//
//                override fun onNext(response: LeaveListResponse) {
//                    val newList = response.leave_data ?: emptyList()
//                    if (newList.isNotEmpty()) {
//                        leaveList = newList
//                        adapter.updateList(leaveList)
//                        status = response.status!!.toInt()
//                        showLayout()
//                    } else {
//                        Toast.makeText(this@LeaveStatusActivity, response.message.toString(), Toast.LENGTH_SHORT).show()
//                        status = -1
//                        showLayout()
//                    }
//                }
//
//                override fun onError(e: Throwable) {
//                    Toast.makeText(this@LeaveStatusActivity, "Error fetching data", Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onComplete() {}
//            })
//    }

//    private fun deleteLeave(leaveItem: LeaveDataItem) {
//        val apiService = RetrofitClient.getInstance()
//        val companyId = preferenceManager.getCompanyId()
//        val userId = preferenceManager.getUserId()
//
//        leaveItem.l_id?.let {
//            apiService.deleteLeave("delete", it, userId, companyId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Observer<LeaveDeteleResponse> {
//                    override fun onSubscribe(d: Disposable) {}
//
//                    override fun onNext(response: LeaveDeteleResponse) {
//                        leaveList = leaveList.filter { it?.leaveId != leaveItem.l_id }
//                        adapter.updateList(leaveList)
//                        callLeaveList()
//                        showLayout()
//                    }
//
//                    override fun onError(e: Throwable) {
//                        Toast.makeText(
//                            this@LeaveStatusActivity,
//                            "Failed to delete leave",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                    override fun onComplete() {}
//                })
//        }
//    }

    fun showLayout() {
        if (status == 200) {
            binding.noDataLayout.visibility = View.GONE
            binding.recyclerLeaveTracker.visibility = View.VISIBLE
        } else {
            binding.noDataLayout.visibility = View.VISIBLE
            binding.recyclerLeaveTracker.visibility = View.GONE
        }

    }

}
