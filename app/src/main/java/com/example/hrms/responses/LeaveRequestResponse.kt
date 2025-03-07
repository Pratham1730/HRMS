package com.example.hrms.responses

import java.io.Serializable

data class LeaveRequestResponse(
	val message: String? = null,
	val status: Int? = null
) : Serializable

