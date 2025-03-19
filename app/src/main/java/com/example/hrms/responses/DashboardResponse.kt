package com.example.hrms.responses

data class DashboardResponse(
	val message: String? = null,
	val attendance: List<AttendanceItem?>? = null,
	val status: Int? = null
)

data class AttendanceItem(
	val u_id: String? = null,
	val a_date: String? = null,
	val a_punch_out_time: Any? = null,
	val a_punch_in_time: String? = null
)

