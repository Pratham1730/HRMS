package com.example.hrms.responses

import java.io.Serializable

data class AttendanceResponse(
	val year: Int? = null,
	val original_salary: Int? = null,
	val paid_leave_days: Int? = null,
	val unpaid_leave_days: Int? = null,
	val u_id: Int? = null,
	val month: Int? = null,
	val final_salary: Any? = null,
	val per_day_salary: Any? = null,
	val total_deduction: Any? = null,
	val half_days: Int? = null,
	val present_days: Int? = null,
	val absent_days: Int? = null,
	val status: Int? = null
) : Serializable

