package com.example.hrms.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hrms.RetrofitClient
import com.example.hrms.databinding.ActivityForgetPasswordBinding
import com.example.hrms.responses.ApiResponse
import com.example.hrms.responses.ApiResponseOtp
import com.example.hrms.responses.UpdatePasswordResponse
import com.example.hrms.responses.VerifyOtpResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import java.util.regex.Pattern

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding
    private var baseUrl = "http://192.168.4.140/"
    private var otpMessage = ""
    private var otpVerifyMessage = ""

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

        binding.btnSendOtp.setOnClickListener {
            if (binding.edtForgotPasswordEmail.text.toString().isEmpty()) {
                binding.edtForgotPasswordEmail.requestFocus()
                binding.edtForgotPasswordEmail.error = "Please add email"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtForgotPasswordEmail.text.toString())
                    .matches()
            ) {
                Toast.makeText(this, "Email not verified", Toast.LENGTH_SHORT).show()
            } else if (binding.edtForgotPasswordMobile.text.toString().isEmpty()) {
                binding.edtForgotPasswordMobile.requestFocus()
                binding.edtForgotPasswordMobile.error = "Please add phone number"
            } else {
                getOtp()
            }
        }

        binding.btnVerifyOtp.setOnClickListener {
            verifyOtp()
        }

        binding.btnSetPassword.setOnClickListener {
            validationsForPassword()
        }
    }


    private fun validationsForPassword(){
        if (binding.edtEnterNewPassword.text.toString().isEmpty()) {
            binding.edtEnterNewPassword.requestFocus()
            binding.edtEnterNewPassword.error = "Please add password"
        } else if (!isValidPassword(password = binding.edtEnterNewPassword.text.toString())) {
            binding.edtEnterNewPassword.requestFocus()
            binding.edtEnterNewPassword.error =
                "Password must have at least 8 characters, 1 uppercase, 1 lowercase, 1 digit, and 1 special character"
        } else if (binding.edtConfirmNewPassword.text.toString().isEmpty()) {
            binding.edtConfirmNewPassword.requestFocus()
            binding.edtConfirmNewPassword.error = "Confirm your password"
        } else if (binding.edtEnterNewPassword.text.toString() != binding.edtConfirmNewPassword.text.toString()) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            binding.edtConfirmNewPassword.requestFocus()
            binding.edtConfirmNewPassword.error = "Passwords do not match"
        } else {
            setNewPassword()
            finish()
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

                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

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
                            otpMessage = responseString
                            ApiResponseOtp(message = responseString)
                        }

                        if (apiResponse.status == null) {
                            Toast.makeText(
                                this@ForgetPasswordActivity,
                                apiResponse.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
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

    private fun verifyOtp() {

        var email = binding.edtForgotPasswordEmail.text.toString()
        var otp = binding.edtEnterOtp.text.toString().toInt()

        val apiService = RetrofitClient.getInstance(baseUrl)

        apiService.verifyOtp(email, otp)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<VerifyOtpResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {

                }

                override fun onNext(t: VerifyOtpResponse) {
                    otpVerifyMessage = t.message.toString()
                    Toast.makeText(
                        this@ForgetPasswordActivity,
                        t.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    if (otpVerifyMessage == "OTP Verified Successfully!") {
                        showPasswordFields()
                    }
                }
            })
    }

    private fun showPasswordFields() {
        binding.btnSendOtp.visibility = View.GONE
        binding.btnVerifyOtp.visibility = View.GONE
        binding.enterOtpField.visibility = View.GONE
        binding.edtForgotPasswordMobile.isEnabled = false
        binding.edtForgotPasswordEmail.isEnabled = false
        binding.llPasswordFields.visibility = View.VISIBLE
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+$).{8,}$"
        )
        return passwordPattern.matcher(password).matches()
    }

    private fun setNewPassword() {
        var email = binding.edtForgotPasswordEmail.text.toString()
        val password = binding.edtEnterNewPassword.text.toString()

        val apiService = RetrofitClient.getInstance(baseUrl)

        apiService.(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UpdatePasswordResponse>{
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@ForgetPasswordActivity, "error", Toast.LENGTH_SHORT).show()
                }

                override fun onComplete() {
                }

                override fun onNext(t: UpdatePasswordResponse) {
                    Toast.makeText(this@ForgetPasswordActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })

    }
}