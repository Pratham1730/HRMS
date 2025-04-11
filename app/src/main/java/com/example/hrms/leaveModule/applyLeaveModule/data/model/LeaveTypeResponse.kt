package com.example.hrms.leaveModule.applyLeaveModule.data.model

import java.io.Serializable

data class LeaveTypeResponse(
	val leave_types: List<LeaveTypesItem?>? = null,
	val message: String? = null,
	val status: Int? = null
) : Serializable

data class LeaveTypesItem(
	val type_name: String? = null,
	val id: String? = null
) : Serializable

