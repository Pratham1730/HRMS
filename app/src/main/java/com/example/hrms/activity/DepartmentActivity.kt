package com.example.hrms.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hrms.R
import com.example.hrms.RetrofitClient
import com.example.hrms.adapter.CustomSpinnerAdapter
import com.example.hrms.databinding.ActivityDepartmentBinding
import com.example.hrms.preferences.PreferenceManager
import com.example.hrms.responses.DepartmentModel
import com.example.hrms.responses.DepartmentsItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class DepartmentActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDepartmentBinding
    private lateinit var preferenceManager: PreferenceManager
    private var companyId = 0
    private var department: String = "Department"
    private lateinit var departmentList: List<DepartmentsItem?>
    private var departmentId = -1
    private val baseUrl = "http://192.168.4.140/"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDepartmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this@DepartmentActivity)

        binding.btnDepartmentBack.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }

        callDept()

    }

    private fun departmentSpinner() {
        if (departmentList.isEmpty()) return

        val departmentArray = Array(departmentList.size + 1) { "" }
        departmentArray[0] = "Department"  // Default value

        for (i in departmentList.indices) {
            departmentArray[i + 1] = departmentList[i]?.deptName ?: "Unknown"
        }

        val adapter = CustomSpinnerAdapter(this, departmentArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.departmentPageSpinner.adapter = adapter

        binding.departmentPageSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        department = departmentArray[position]
                        departmentId =
                            departmentList[position - 1]?.deptId.toString().toInt()
                    } else {
                        departmentId = -1
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun callDept() {
        companyId = preferenceManager.getCompanyId()
        val apiService = RetrofitClient.getInstance(baseUrl)

        apiService.setDept("select_dept", companyId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<DepartmentModel> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }

                override fun onNext(t: DepartmentModel) {

                    val newList = t.departments ?: emptyList()

                    if (newList.isNotEmpty()) {
                        departmentList = newList
                        departmentSpinner()
                    }
                    else {
                        Toast.makeText(
                            this@DepartmentActivity,
                            t.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }


}