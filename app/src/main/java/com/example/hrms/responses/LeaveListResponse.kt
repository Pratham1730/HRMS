package com.example.hrms.responses

data class LeaveListResponse(
	val message: String? = null,
	val leaveData: List<LeaveDataItem?>? = null,
	val status: Int? = null
)

data class LeaveDataItem(
	val l_id: Int? = null,
	val u_id: Int? = null,
	val lReason: String? = null,
	val company_id: Int? = null,
	val leaveType: String? = null,
	val leaveStatus: String? = null,
	val lStartDate: String? = null,
	val lId: Int? = null,
	val lApprovedBy: Any? = null,
	val leaveTypeId: Int? = null
)

