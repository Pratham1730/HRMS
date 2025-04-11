package com.example.hrms.holidaysModule.presentation.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hrms.RetrofitClient
import com.example.hrms.common.ApiResultState
import com.example.hrms.holidaysModule.presentation.adapter.HolidaysAdapter
import com.example.hrms.databinding.ActivityHolidayBinding
import com.example.hrms.preferences.PreferenceManager
import com.example.hrms.holidaysModule.data.model.HolidaysItem
import com.example.hrms.holidaysModule.data.model.PublicHolidaysResponse
import com.example.hrms.holidaysModule.domain.model.response.HolidaysDomainItem
import com.example.hrms.holidaysModule.presentation.viewModels.HolidaysViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HolidayActivity : AppCompatActivity() {

    private val viewModel : HolidaysViewModel by viewModels()

    private lateinit var binding : ActivityHolidayBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var adapter : HolidaysAdapter
    private var holidayList : List<HolidaysDomainItem?> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHolidayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        binding.holidayListRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = HolidaysAdapter(this@HolidayActivity , holidayList)
        binding.holidayListRv.adapter = adapter

        getHolidays()

        listeners()
    }

    private fun listeners(){
        binding.imgBackHolidayPage.setOnClickListener {
            finish()
        }
    }

    private fun getHolidays(){
        preferenceManager = PreferenceManager(this)
        val companyId = preferenceManager.getCompanyId()
        viewModel.loadHolidays("fetch_holidays" , companyId)
    }

    private fun observeHolidays(){
        viewModel.holidaysResponse.observe(this){result ->
            when(result){
                is ApiResultState.Loading -> {
                    // Optional: Show loading UI
                }
                is ApiResultState.Success -> {
                    val newList = result.data.holidays ?: emptyList()
                    if (newList.isNotEmpty()) {
                        holidayList = newList
                        adapter.updateList(holidayList)
                    } else {
                        Toast.makeText(this@HolidayActivity, "No Leave Record Found", Toast.LENGTH_SHORT).show()
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

//    private fun getHolidays(){
//        preferenceManager = PreferenceManager(this)
//        val companyId = preferenceManager.getCompanyId()
//
//        val apiService = RetrofitClient.getInstance()
//
//        apiService.getHolidays("fetch_holidays", companyId)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<PublicHolidaysResponse>{
//                override fun onSubscribe(d: Disposable) {
//
//                }
//
//                override fun onError(e: Throwable) {
//                    Toast.makeText(this@HolidayActivity, "Error", Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onComplete() {
//                }
//
//                override fun onNext(t: PublicHolidaysResponse) {
//                    val newList = t.holidays ?: emptyList()
//                    if (newList.isNotEmpty()) {
//                        holidayList = newList
//                        adapter.updateList(holidayList)
//                    } else {
//                        Toast.makeText(this@HolidayActivity, "No Leave Record Found", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            })
//    }
}