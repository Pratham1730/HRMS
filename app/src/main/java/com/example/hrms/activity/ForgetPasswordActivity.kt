package com.example.hrms.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hrms.RetrofitClient
import com.example.hrms.databinding.ActivityForgetPasswordBinding
import com.example.hrms.responses.ApiResponse
import com.example.hrms.responses.ApiResponseOtp
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding
    private var baseUrl = "http://192.168.4.140/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listeners()


    }

    private fun listeners() {
        binding.forgotPasswordBack.setOnClickListener {
            finish()
        }

        binding.btnForgotPasswordVerify.setOnClickListener {
            getOtp()
        }
    }

    private fun getOtp() {
        var email = binding.edtForgotPasswordEmail.text.toString()
        var phone = binding.edtForgotPasswordMobile.text.toString().toBigInteger()
        val apiService = RetrofitClient.getInstance(baseUrl)

        apiService.getOtp("generateotp", email, phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseBody> {
                override fun onSubscribe(d: Disposable) {
                    Toast.makeText(this@ForgetPasswordActivity, "Sub", Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@ForgetPasswordActivity, "Error", Toast.LENGTH_SHORT).show()
                }

                override fun onComplete() {
                    Toast.makeText(this@ForgetPasswordActivity, "Complete", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onNext(t: ResponseBody) {
                    try {
                        val responseString = t.string()

                        val apiResponse = try {
                            val jsonObject = JSONObject(responseString)
                            ApiResponseOtp(
                                message = jsonObject.optString("message", "Unknown message"),
                                status = jsonObject.optInt("status", -1).takeIf { it != -1 }
                            )
                        } catch (e: Exception) {
                            ApiResponseOtp(message = responseString)
                        }

                        if (apiResponse.status == null) {
                            Toast.makeText(
                                this@ForgetPasswordActivity,
                                apiResponse.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Handle error case
                            Toast.makeText(
                                this@ForgetPasswordActivity,
                                "Error: ${apiResponse.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@ForgetPasswordActivity,
                            "Parsing error: ${e.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })

    }


}