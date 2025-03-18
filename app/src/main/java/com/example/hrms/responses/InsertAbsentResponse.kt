package com.example.hrms.responses

import java.io.Serializable

data class InsertAbsentResponse(
	val message: String? = null,
	val status: Int? = null
) : Serializable

