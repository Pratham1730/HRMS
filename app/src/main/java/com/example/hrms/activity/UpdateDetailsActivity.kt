package com.example.hrms.activity

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.hrms.RetrofitClient
import com.example.hrms.databinding.ActivityUpdateDetailsBinding
import com.example.hrms.preferences.PreferenceManager
import com.example.hrms.responses.UpdateDataResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class UpdateDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateDetailsBinding
    private var selectedCalendarDOB: Calendar = Calendar.getInstance()
    private lateinit var preferenceManager: PreferenceManager
    private var userId : Int = -1
    private  var imageUri: Uri ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        preferenceManager = PreferenceManager(this@UpdateDetailsActivity)

        listeners()

        setData()
    }

    private fun listeners(){
        binding.edtUpdateProfileDOB.setOnClickListener {
            onClickDateDOB()
        }
        binding.btnUpdatePageUpdate.setOnClickListener {
            validations()
        }
        binding.imgUpdateProfileBack.setOnClickListener {
            finish()
        }
        binding.imgUpdateProfile.setOnClickListener {
            showImagePickerDialog()
        }
    }

    private fun setData(){
        userId = preferenceManager.getUserId()
        (binding.edtUpdateProfileName as TextView).text  = preferenceManager.getUserName().toString()
        (binding.edtUpdateProfileDOB as TextView).text = preferenceManager.getUserDOB().toString()
        (binding.edtUpdateProfilePhone as TextView).text = preferenceManager.getUserPhone().toString()
    }

    private fun validations(){

        if (binding.edtUpdateProfileName.text.toString().isEmpty()) {
            binding.edtUpdateProfileName.requestFocus()
            binding.edtUpdateProfileName.error = "Please add full name"
        }
        else if (binding.edtUpdateProfilePhone.text.toString().isEmpty()) {
            binding.edtUpdateProfilePhone.requestFocus()
            binding.edtUpdateProfilePhone.error = "Please add phone number"
        }
        else if(binding.edtUpdateProfilePhone.text.toString().length < 10){
            binding.edtUpdateProfilePhone.requestFocus()
            binding.edtUpdateProfilePhone.error = "Phone number can't be less tha 10 digits"
        }
        else if (binding.edtUpdateProfileDOB.text.toString().isEmpty()) {
            binding.edtUpdateProfileDOB.requestFocus()
            binding.edtUpdateProfileDOB.error = "Please add birth date"
        }
        else{
            updatedData()
            startActivity(Intent(this@UpdateDetailsActivity , ProfileActivity::class.java))
            finish()

        }
    }

    private fun updatedData() {
        val method = createPartFromString("update_user")
        val uIdPart = createPartFromString(userId.toString())
        val namePart = createPartFromString(binding.edtUpdateProfileName.text.toString())
        val phonePart = createPartFromString(binding.edtUpdateProfilePhone.text.toString())
        val dobPart = createPartFromString(binding.edtUpdateProfileDOB.text.toString())

        val imagePart = if (imageUri != null) {
            prepareImagePart(imageUri!!)
        }
        else {
            null
        }

        val apiService = RetrofitClient.getInstance()

        apiService.updateUser(method, uIdPart, namePart, phonePart, dobPart, imagePart)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UpdateDataResponse> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Toast.makeText(this@UpdateDetailsActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onComplete() {}

                override fun onNext(t: UpdateDataResponse) {
                    preferenceManager.saveUserName(binding.edtUpdateProfileName.text.toString())
                    preferenceManager.saveUserPhone(binding.edtUpdateProfilePhone.text.toString())
                    preferenceManager.saveUserDOB(binding.edtUpdateProfileDOB.text.toString())
                    Toast.makeText(this@UpdateDetailsActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun createPartFromString(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun prepareImagePart(uri: Uri): MultipartBody.Part {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(cacheDir, "upload_${System.currentTimeMillis()}.jpg")
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("user_img", file.name, reqFile)
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

        val datePickerDialog = DatePickerDialog(
            this, dateSetListener,
            c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.maxDate = c.timeInMillis

        datePickerDialog.show()
    }


    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                binding.imgUpdateProfile.setImageURI(it)
            }
        }

    private val captureImageLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success && imageUri != null) {
                binding.imgUpdateProfile.setImageURI(imageUri)
            }
        }

    private fun pickImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun captureImageFromCamera() {
        val file = File(this@UpdateDetailsActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo.jpg")
        imageUri = FileProvider.getUriForFile(this@UpdateDetailsActivity, "${this@UpdateDetailsActivity.packageName}.fileprovider", file)
        captureImageLauncher.launch(imageUri!!)
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = android.app.AlertDialog.Builder(this@UpdateDetailsActivity)
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
                Toast.makeText(this@UpdateDetailsActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    private fun checkPermissionsAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this@UpdateDetailsActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            captureImageFromCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

}