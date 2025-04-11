package com.example.hrms.userProfileModule.presentation.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.content.pm.ActivityInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hrms.RetrofitClient
import com.example.hrms.activity.UpdateDetailsActivity
import com.example.hrms.common.ApiResultState
import com.example.hrms.databinding.ActivityProfileBinding
import com.example.hrms.preferences.PreferenceManager
import com.example.hrms.userProfileModule.data.model.UserDataResponse
import com.example.hrms.signInModule.presentation.view.SignInActivity
import com.example.hrms.userProfileModule.presentation.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private val viewModel : ProfileViewModel by viewModels()

    private lateinit var binding : ActivityProfileBinding
    private lateinit var email : String
    private lateinit var preferenceManager: PreferenceManager




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        preferenceManager = PreferenceManager(this@ProfileActivity)

        //setUserData()

        getUserData()
        observeUserData()

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

    private fun getUserData(){
        email = preferenceManager.getUserEmail().toString()
        viewModel.userData("get_user_by_email" , email)
    }

    private fun observeUserData() {
        viewModel.user.observe(this) { t  ->
            when (t ) {
                is ApiResultState.Loading -> {
                    // Optional: Show loading UI
                }
                is ApiResultState.Success -> {
                    binding.txtProfileName.text = t.data.user?.userName
                    binding.txtProfilePhone.text = t.data.user?.phone
                    binding.txtProfileEmail.text = t.data.user?.email
                    binding.txtProfileDB.text = t.data.user?.dateOfBirth
                    binding.txtProfileJoiningDate.text = t.data.user?.joiningDate
                    binding.txtProfileDepartment.text = t.data.user?.departmentName
                    binding.txtProfilePosition.text = t.data.user?.positionName
                    binding.txtProfileSalary.text = t.data.user?.salary

                    if (t.data.user?.gender.toString().toInt() == 1){
                        binding.txtProfileGender.text = "Male"
                    }
                    else{
                        binding.txtProfileGender.text = "Female"
                    }
                }
                is ApiResultState.ApiError -> {
                    Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
                }

                is ApiResultState.ServerError -> {

                }
            }
        }
    }

//    private fun setUserData(){
//        email = preferenceManager.getUserEmail().toString()
//        val apiService = RetrofitClient.getInstance()
//
//        apiService.getUserData("get_user_by_email" , email)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object  : Observer<UserDataResponse>{
//                override fun onSubscribe(d: Disposable) {
//
//                }
//
//                override fun onError(e: Throwable) {
//                }
//
//                override fun onComplete() {
//                }
//
//                override fun onNext(t: UserDataResponse) {
//                    binding.txtProfileName.text = t.user?.u_name.toString()
//                    binding.txtProfilePhone.text = t.user?.u_phone.toString()
//                    binding.txtProfileEmail.text = t.user?.u_email.toString()
//                    binding.txtProfileDB.text = t.user?.u_dob.toString()
//                    binding.txtProfileJoiningDate.text = t.user?.u_joining_Date.toString()
//                    binding.txtProfileDepartment.text = t.user?.dept_name.toString()
//                    binding.txtProfilePosition.text = t.user?.position_name.toString()
//
////                    val rawUrl = t.user!!.u_img
////                    val fixedUrl = rawUrl?.replace("\\", "")
////                    preferenceManager.saveUserImageUrl(fixedUrl!!)
////
////
////                    Glide.with(this@ProfileActivity)
////                        .load(fixedUrl)
////                        .placeholder(R.drawable.placeholder)
////                        .into(binding.imgProfile)
//                    if (t.user?.u_gender.toString().toInt() == 1){
//                        binding.txtProfileGender.text = "Male"
//                    }
//                    else{
//                        binding.txtProfileGender.text = "Female"
//                    }
//                    binding.txtProfileSalary.text = t.user?.u_salary.toString()
//
//                    preferenceManager.saveUserName(t.user?.u_name.toString())
//                    preferenceManager.saveUserPhone(t.user?.u_phone.toString())
//                    preferenceManager.saveUserDOB(t.user?.u_dob.toString())
//
//                }
//            })
//
//    }



}