package com.example.hrms.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hrms.responses.UserDataResponse
import com.example.hrms.RetrofitClient
import com.example.hrms.databinding.ActivityProfileBinding
import com.example.hrms.preferences.PreferenceManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding
    private lateinit var email : String
    private lateinit var preferenceManager: PreferenceManager
    private val baseUrl = "http://192.168.4.140/"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this@ProfileActivity)

        setUserData()

        listener()

        binding.imgProfileBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }


        binding.btnProfileLogOut.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { dialog, _ ->
                    preferenceManager.logout()
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                .show()
        }

    }

    private fun listener(){
        binding.btnProfileUpdate.setOnClickListener {
            startActivity(Intent(this@ProfileActivity , UpdateDetailsActivity::class.java))
        }
        binding.imgProfile.setOnClickListener {
            finish()
        }
    }

    private fun setUserData(){
        email = preferenceManager.getUserEmail().toString()
        val apiService = RetrofitClient.getInstance(baseUrl)

        apiService.getUserData("get_user_by_email" , email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object  : Observer<UserDataResponse>{
                override fun onSubscribe(d: Disposable) {
                    Toast.makeText(this@ProfileActivity, "Sub", Toast.LENGTH_SHORT).show()

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@ProfileActivity, "Error", Toast.LENGTH_SHORT).show()
                }

                override fun onComplete() {
                }

                override fun onNext(t: UserDataResponse) {
                    Toast.makeText(this@ProfileActivity, "Success", Toast.LENGTH_SHORT).show()
                    binding.txtProfileName.text = t.user?.u_name.toString()
                    binding.txtProfilePhone.text = t.user?.u_phone.toString()
                    binding.txtProfileEmail.text = t.user?.u_email.toString()
                    binding.txtProfileDB.text = t.user?.u_dob.toString()
                    binding.txtProfileJoiningDate.text = t.user?.u_joining_Date.toString()
                    binding.txtProfileDepartment.text = t.user?.dept_name.toString()
                    binding.txtProfilePosition.text = t.user?.position_name.toString()
                    binding.txtProfileGender.text = t.user?.u_gender.toString()
                    binding.txtProfileSalary.text = t.user?.u_salary.toString()

                    preferenceManager.saveUserName(t.user?.u_name.toString())
                    preferenceManager.saveUserPhone(t.user?.u_phone.toString())
                    preferenceManager.saveUserDOB(t.user?.u_dob.toString())

                }
            })

    }
}