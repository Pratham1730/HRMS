package com.example.hrms.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hrms.R
import com.example.hrms.RetrofitClient
import com.example.hrms.adapter.HolidaysAdapter
import com.example.hrms.databinding.ActivityHolidayBinding
import com.example.hrms.preferences.PreferenceManager
import com.example.hrms.responses.HolidaysItem
import com.example.hrms.responses.PublicHolidaysResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HolidayActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHolidayBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var adapter : HolidaysAdapter
    private var holidayList : List<HolidaysItem?> = emptyList()


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

        val apiService = RetrofitClient.getInstance()

        apiService.getHolidays("fetch_holidays", companyId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<PublicHolidaysResponse>{
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@HolidayActivity, "Error", Toast.LENGTH_SHORT).show()
                }

                override fun onComplete() {
                }

                override fun onNext(t: PublicHolidaysResponse) {
                    val newList = t.holidays ?: emptyList()
                    if (newList.isNotEmpty()) {
                        holidayList = newList
                        adapter.updateList(holidayList)
                    } else {
                        Toast.makeText(this@HolidayActivity, "No Leave Record Found", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }
}