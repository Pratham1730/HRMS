package com.example.hrms.Models

data class LoginResponse(
	val message: String? = null,
	val user: User? = null,
	val status: Int? = null
)

data class User(
	val uName: String? = null,
	val uId: Int? = null,
	val uEmail: String? = null
)

