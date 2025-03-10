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
import com.example.hrms.databinding.ActivitySelectCompanyBinding
import com.example.hrms.responses.CompanyItem
import com.example.hrms.responses.CompanyResponse
import com.example.hrms.responses.DepartmentsItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class SelectCompanyActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySelectCompanyBinding
    private lateinit var companyList: List<CompanyItem?>
    private var companyId = -1
    private val baseUrl = "http://192.168.4.140/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        callCompany()
        listener()

    }

    private fun listener(){
        binding.btnSelectCompanyNext.setOnClickListener {
            if (companyId == 0){
                Toast.makeText(this@SelectCompanyActivity, "Enter Correct Company", Toast.LENGTH_SHORT).show()
            }
            else{
                val intent = Intent(this@SelectCompanyActivity , SignUpActivity::class.java)
                intent.putExtra("COMPANY_ID" , companyId)
                startActivity(intent)
            }

        }
    }

    private fun companySpinner() {
        if (companyList.isEmpty()) return

        val companyArray = Array(companyList.size + 1) { "" }
        companyArray[0] = "Company"  // Default value

        for (i in companyList.indices) {
            companyArray[i + 1] = companyList[i]?.company_name ?: "Unknown"
        }

        val adapter = CustomSpinnerAdapter(this, companyArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.companySpinner.adapter = adapter

        binding.companySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    companyId =
                        companyList[position - 1]?.company_id.toString().toInt()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun callCompany(){
        val apiService = RetrofitClient.getInstance(baseUrl)

        apiService.selectCompany("select_company")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CompanyResponse>{
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@SelectCompanyActivity, "Error", Toast.LENGTH_SHORT).show()
                }

                override fun onComplete() {
                }

                override fun onNext(t: CompanyResponse) {
                    companyList = t.company!!
                    companySpinner()
                }
            })
    }
}