package com.example.hrms.responses

import java.io.Serializable

data class ApiResponse(
    val status: Int,
    val message: String
) : Serializable

