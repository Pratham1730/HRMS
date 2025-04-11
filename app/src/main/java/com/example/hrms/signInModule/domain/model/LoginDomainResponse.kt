package com.example.hrms.signInModule.domain.model

import java.io.Serializable

data class LoginDomainResponse(
	val message: String? = null,
	val user: UserDomain? = null,
	val status: Int? = null
) : Serializable

data class UserDomain(
	val uName: String? = null,
	val uId: Int? = null,
	val uEmail: String? = null,
	val companyId : Int? = null
) : Serializable

