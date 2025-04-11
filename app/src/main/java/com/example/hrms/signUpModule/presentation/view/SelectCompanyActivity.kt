package com.example.hrms.signUpModule.presentation.view

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hrms.RetrofitClient
import com.example.hrms.adapter.CustomSpinnerAdapter
import com.example.hrms.databinding.ActivitySelectCompanyBinding
import com.example.hrms.responses.CompanyItem
import com.example.hrms.responses.CompanyResponse
import com.example.hrms.common.ApiResultState
import com.example.hrms.signUpModule.domain.model.CompanyDomainItem
import com.example.hrms.signUpModule.presentation.viewModel.SelectCompanyViewModel
import com.example.hrms.signUpModule.presentation.viewModel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

@AndroidEntryPoint
class SelectCompanyActivity : AppCompatActivity() {

    private val viewModel : SelectCompanyViewModel by viewModels()


    private lateinit var binding : ActivitySelectCompanyBinding
    private lateinit var companyList: List<CompanyDomainItem?>
    private var companyId = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)


        getCompany()
        observerCompanyList()
        listener()

    }

    private fun listener(){
        binding.btnSelectCompanyNext.setOnClickListener {
            if (companyId == -1){
                Toast.makeText(this@SelectCompanyActivity, "Enter Correct Company", Toast.LENGTH_SHORT).show()
            }
            else{
                val intent = Intent(this@SelectCompanyActivity , SignUpActivity::class.java)
                intent.putExtra("COMPANY_ID" , companyId)
                startActivity(intent)
                finish()
            }
        }
        binding.btnSelectCompanyBack.setOnClickListener {
            finish()
        }
    }

    fun getCompany(){
        viewModel.loadCompanies("select_company")
    }

    private fun observerCompanyList(){
        viewModel.company.observe(this){result ->
            when (result ) {
                is ApiResultState.Loading -> {
                    // Optional: Show loading UI
                }
                is ApiResultState.Success -> {
                    viewModel.companyList.observe(this){ companyItem ->
                        companyList = companyItem!!
                        companySpinner()
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

    private fun companySpinner() {
        if (companyList.isEmpty()) return

        val companyArray = Array(companyList.size + 1) { "" }
        companyArray[0] = "Company"  // Default value

        for (i in companyList.indices) {
            companyArray[i + 1] = companyList[i]?.companyName ?: "Unknown"
        }

        val adapter = CustomSpinnerAdapter(this, companyArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.companySpinner.adapter = adapter

        binding.companySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    companyId =
                        companyList[position - 1]?.companyId.toString().toInt()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                companyId = -1
            }
        }
    }

//    private fun callCompany(){
//        val apiService = RetrofitClient.getInstance()
//
//        apiService.selectCompany("select_company")
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<CompanyResponse>{
//                override fun onSubscribe(d: Disposable) {
//
//                }
//
//                override fun onError(e: Throwable) {
//                    Toast.makeText(this@SelectCompanyActivity, "Error", Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onComplete() {
//                }
//
//                override fun onNext(t: CompanyResponse) {
//                    companyList = t.company!!
//                    companySpinner()
//                }
//            })
//    }
}