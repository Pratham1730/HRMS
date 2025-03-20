package com.example.hrms.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrms.RetrofitClient
import com.example.hrms.adapter.CustomSpinnerAdapter
import com.example.hrms.adapter.DeptEmployeeAdapter
import com.example.hrms.databinding.ActivityDepartmentBinding
import com.example.hrms.preferences.PreferenceManager
import com.example.hrms.responses.DepartmentEmployeeResponse
import com.example.hrms.responses.DepartmentModel
import com.example.hrms.responses.DepartmentsItem
import com.example.hrms.responses.EmployeesItem
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
    private var isDeptSelected = false
    private var departmentId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDepartmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        preferenceManager = PreferenceManager(this@DepartmentActivity)

        listeners()

        binding.rvEmployees.layoutManager = LinearLayoutManager(this)

        callDept()
    }

    private fun listeners(){
        binding.btnDepartmentBack.setOnClickListener {
            finish()
        }
    }

    private fun departmentSpinner() {
        if (departmentList.isEmpty()) return

        val departmentArray = Array(departmentList.size + 1) { "" }
        departmentArray[0] = "Department"

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
                        departmentId = departmentList[position - 1]?.deptId.toString().toInt()
                        callDeptEmployees()
                    } else {
                        binding.noDataLayoutDept.visibility = View.VISIBLE
                        binding.rvEmployees.visibility = View.GONE
                        departmentId = -1
                        binding.rvEmployees.adapter =
                            null
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun callDept() {
        companyId = preferenceManager.getCompanyId()
        val apiService = RetrofitClient.getInstance()

        apiService.setDept("select_dept", companyId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<DepartmentModel> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Toast.makeText(
                        this@DepartmentActivity,
                        "Failed to load departments",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onComplete() {}

                override fun onNext(t: DepartmentModel) {
                    val newList = t.departments ?: emptyList()
                    if (newList.isNotEmpty()) {
                        departmentList = newList
                        departmentSpinner()
                    } else {
                        Toast.makeText(
                            this@DepartmentActivity,
                            t.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    private fun callDeptEmployees() {
        if (departmentId == -1) {
            return
        }

        val apiService = RetrofitClient.getInstance()

        apiService.departmentEmployee("select", departmentId, companyId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<DepartmentEmployeeResponse> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Toast.makeText(
                        this@DepartmentActivity,
                        "Failed to load employees",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onComplete() {}

                override fun onNext(response: DepartmentEmployeeResponse) {
                    if (!response.employees.isNullOrEmpty()) {
                        binding.rvEmployees.visibility = View.VISIBLE
                        binding.noDataLayoutDept.visibility = View.GONE
                        setupEmployeeRecyclerView(response.employees)
                    } else {
                        binding.noDataLayoutDept.visibility = View.VISIBLE
                        binding.rvEmployees.visibility = View.GONE
                        binding.rvEmployees.adapter = null
                    }
                }
            })
    }

    private fun setupEmployeeRecyclerView(employeeList: List<EmployeesItem?>) {
        val adapter = DeptEmployeeAdapter(this, employeeList.filterNotNull())
        binding.rvEmployees.adapter = adapter
    }
}
