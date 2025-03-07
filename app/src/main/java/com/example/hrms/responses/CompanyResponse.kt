package com.example.hrms.responses

import java.io.Serializable

data class CompanyResponse(
	val company: List<CompanyItem?>? = null,
	val message: String? = null,
	val status: Int? = null
) : Serializable

data class CompanyItem(
	val company_id: String? = null,
	val company_name: String? = null
) : Serializable

