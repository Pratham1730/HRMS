package com.example.hrms.responses

import java.io.Serializable

data class ApiResponseOtp(
    val message: String,
    val status: Int? = null
) : Serializable