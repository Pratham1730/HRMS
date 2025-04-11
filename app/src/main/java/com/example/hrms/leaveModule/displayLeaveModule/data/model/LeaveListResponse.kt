package com.example.hrms.leaveModule.displayLeaveModule.data.model

import java.io.Serializable

data class LeaveListResponse(
	val message: String? = null,
	val leave_data: List<LeaveDataItem?>? = null,
	val status: Int? = null
) : Serializable

data class LeaveDataItem(
	val l_status_id: Int? = null,
	val u_id: Int? = null,
	val l_reason: String? = null,
	val company_id: Int? = null,
	val leave_type: String? = null,
	val leave_status: String? = null,
	val l_start_date: String? = null,
	val l_id: Int? = null,
	val l_approved_by: Any? = null,
	val leave_type_id: Int? = null
) : Serializable


