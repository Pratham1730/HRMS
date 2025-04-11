package com.example.hrms.holidaysModule.domain.model.response

import java.io.Serializable

data class PublicHolidaysDomainResponse(
    val holidays: List<HolidaysDomainItem?>? = null,
    val message: String? = null,
    val status: Int? = null
) : Serializable

data class HolidaysDomainItem(
    val holidayDate: String? = null,
    val holidayName: String? = null
) : Serializable
