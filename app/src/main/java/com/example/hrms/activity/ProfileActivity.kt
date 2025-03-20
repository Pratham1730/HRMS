package com.example.hrms.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hrms.R
import com.example.hrms.RetrofitClient
import com.example.hrms.databinding.ActivityProfileBinding
import com.example.hrms.preferences.PreferenceManager
import com.example.hrms.responses.UserDataResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


class ProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding
    private lateinit var email : String
    private lateinit var preferenceManager: PreferenceManager




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        preferenceManager = PreferenceManager(this@ProfileActivity)

        setUserData()

        listener()

        binding.imgProfileBack.setOnClickListener {
            finish()
        }


        binding.btnProfileLogOut.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { dialog, _ ->
                    preferenceManager.logout()
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                .show()
        }

    }

    private fun listener(){
        binding.btnProfileUpdate.setOnClickListener {
            startActivity(Intent(this@ProfileActivity , UpdateDetailsActivity::class.java))
            finish()
        }

    }

    private fun setUserData(){
        email = preferenceManager.getUserEmail().toString()
        val apiService = RetrofitClient.getInstance()

        apiService.getUserData("get_user_by_email" , email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object  : Observer<UserDataResponse>{
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }

                override fun onNext(t: UserDataResponse) {
                    binding.txtProfileName.text = t.user?.u_name.toString()
                    binding.txtProfilePhone.text = t.user?.u_phone.toString()
                    binding.txtProfileEmail.text = t.user?.u_email.toString()
                    binding.txtProfileDB.text = t.user?.u_dob.toString()
                    binding.txtProfileJoiningDate.text = t.user?.u_joining_Date.toString()
                    binding.txtProfileDepartment.text = t.user?.dept_name.toString()
                    binding.txtProfilePosition.text = t.user?.position_name.toString()

                    val rawUrl = t.user!!.u_img
                    val fixedUrl = rawUrl?.replace("\\", "")
                    preferenceManager.saveUserImageUrl(fixedUrl!!)


                    Glide.with(this@ProfileActivity)
                        .load(fixedUrl)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.imgProfile)
                    if (t.user?.u_gender.toString().toInt() == 1){
                        binding.txtProfileGender.text = "Male"
                    }
                    else{
                        binding.txtProfileGender.text = "Female"
                    }
                    binding.txtProfileSalary.text = t.user?.u_salary.toString()

                    preferenceManager.saveUserName(t.user?.u_name.toString())
                    preferenceManager.saveUserPhone(t.user?.u_phone.toString())
                    preferenceManager.saveUserDOB(t.user?.u_dob.toString())

                }
            })

    }



}