package com.example.hrms.responses

data class PublicHolidaysResponse(
	val holidays: List<HolidaysItem?>? = null,
	val message: String? = null,
	val status: Int? = null
)

data class HolidaysItem(
	val holiday_date: String? = null,
	val holiday_name: String? = null
)

