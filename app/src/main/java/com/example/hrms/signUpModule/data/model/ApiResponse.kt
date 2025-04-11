package com.example.hrms.signUpModule.data.model

import java.io.Serializable

data class ApiResponse(
    val status: Int,
    val message: String
) : Serializable

