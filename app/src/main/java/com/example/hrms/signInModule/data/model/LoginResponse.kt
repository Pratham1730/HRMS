package com.example.hrms.signInModule.data.model

import java.io.Serializable

data class LoginResponse(
	val message: String? = null,
	val user: User? = null,
	val status: Int? = null
) : Serializable

data class User(
	val u_name: String? = null,
	val u_id: Int? = null,
	val u_email: String? = null,
	val company_id : Int? = null
) : Serializable

