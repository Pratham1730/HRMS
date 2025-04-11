package com.example.hrms.signUpModule.domain.model

import java.io.Serializable

data class CompanyDomainResponse(
	val company: List<CompanyDomainItem?>? = null,
	val message: String? = null,
	val status: Int? = null
) : Serializable

data class CompanyDomainItem(
	val companyId: String? = null,
	val companyName: String? = null
) : Serializable

