package com.example.hrms.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.hrms.responses.UserDataResponse
import com.example.hrms.RetrofitClient
import com.example.hrms.databinding.ActivityProfileBinding
import com.example.hrms.preferences.PreferenceManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding
    private lateinit var email : String
    private lateinit var preferenceManager: PreferenceManager
    private val baseUrl = "http://192.168.4.140/"
    private lateinit var imageUri: Uri




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
            showImagePickerDialog()
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


    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { binding.imgProfile.setImageURI(it) }
        }

    private val captureImageLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                binding.imgProfile.setImageURI(imageUri)
            }
        }

    private fun pickImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun captureImageFromCamera() {
        val file = File(this@ProfileActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo.jpg")
        imageUri = FileProvider.getUriForFile(this@ProfileActivity, "${this@ProfileActivity.packageName}.fileprovider", file)
        captureImageLauncher.launch(imageUri)
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = android.app.AlertDialog.Builder(this@ProfileActivity)
        builder.setTitle("Select Image")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> checkPermissionsAndOpenCamera()
                1 -> pickImageFromGallery()
            }
        }
        builder.show()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                captureImageFromCamera()
            } else {
                Toast.makeText(this@ProfileActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    private fun checkPermissionsAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this@ProfileActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            captureImageFromCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}