package com.example.hrms.responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AttendanceResponse(
	val earned_salary: Any? = null,
	val year: Int? = null,
	val original_salary: Int? = null,
	val paid_leave_days: Int? = null,
	val u_id: Int? = null,
	val month: Int? = null,
	val final_salary: Any? = null,
	val per_day_salary: Any? = null,
	val total_deduction: Int? = null,
	@SerializedName("unpaid leave_days") val unpaidLeaveDays: Int? = null,
	val effective_present_days: Any? = null,
	val half_days: Int? = null,
	val present_days: Int? = null,
	val absent_days: Int? = null,
	val status: Int? = null
) : Serializable

