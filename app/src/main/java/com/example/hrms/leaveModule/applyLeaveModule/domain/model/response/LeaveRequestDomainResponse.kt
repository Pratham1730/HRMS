package com.example.hrms.leaveModule.applyLeaveModule.domain.model.response

import java.io.Serializable

data class LeaveRequestDomainResponse(
	val message: String? = null,
	val status: Int? = null
) : Serializable

