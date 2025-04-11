package com.example.hrms.leaveModule.applyLeaveModule.domain.model.response

import java.io.Serializable

data class LeaveTypeDomainResponse(
	val leaveType: List<LeaveTypesDomainItem?>? = null,
	val message: String? = null,
	val status: Int? = null
) : Serializable

data class LeaveTypesDomainItem(
	val typeName: String? = null,
	val id: String? = null
) : Serializable

