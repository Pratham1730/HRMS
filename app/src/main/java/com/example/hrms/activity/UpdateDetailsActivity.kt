package com.example.hrms.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hrms.RetrofitClient
import com.example.hrms.databinding.ActivityUpdateDetailsBinding
import com.example.hrms.models.UpdateDataModel
import com.example.hrms.preferences.PreferenceManager
import com.example.hrms.responses.UpdateDataResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.Locale

class UpdateDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateDetailsBinding
    private val baseUrl = "http://192.168.4.140/"
    private var selectedCalendarDOB: Calendar = Calendar.getInstance()
    private lateinit var preferenceManager: PreferenceManager
    private var userId : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this@UpdateDetailsActivity)

        listeners()

        setData()
    }

    private fun listeners(){
        binding.edtUpdateProfileDOB.setOnClickListener {
            onClickDateDOB()
        }
        binding.btnUpdatePageUpdate.setOnClickListener {
            updatedData()
            startActivity(Intent(this@UpdateDetailsActivity , ProfileActivity::class.java))
            finish()
        }
        binding.imgUpdateProfileBack.setOnClickListener {
            finish()
        }
    }

    private fun setData(){
        userId = preferenceManager.getUserId()
        (binding.edtUpdateProfileName as TextView).text  = preferenceManager.getUserName().toString()
        (binding.edtUpdateProfileDOB as TextView).text = preferenceManager.getUserDOB().toString()
        (binding.edtUpdateProfilePhone as TextView).text = preferenceManager.getUserPhone().toString()
    }

    private fun updatedData(){
        val method  = "update_user"
        val name = binding.edtUpdateProfileName.text.toString()
        val phone = binding.edtUpdateProfilePhone.text.toString()
        val dob = binding.edtUpdateProfileDOB.text.toString()

        val model = UpdateDataModel(method , userId , name , phone.toBigInteger() , dob )

        val apiService = RetrofitClient.getInstance(baseUrl)

        apiService.updateUser(model)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UpdateDataResponse>{
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }

                override fun onNext(t: UpdateDataResponse) {
                    preferenceManager.saveUserEmail(name)
                    preferenceManager.saveUserPhone(phone)
                    preferenceManager.saveUserDOB(dob)
                    Toast.makeText(this@UpdateDetailsActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun onClickDateDOB() {
        val c = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            selectedCalendarDOB.set(Calendar.YEAR, year)
            selectedCalendarDOB.set(Calendar.MONTH, month)
            selectedCalendarDOB.set(Calendar.DAY_OF_MONTH, day)

            val tDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedCalendarDOB.time)
            binding.edtUpdateProfileDOB.setText(tDate)
        }

        DatePickerDialog(
            this, dateSetListener,
            c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}