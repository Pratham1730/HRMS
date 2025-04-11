package com.example.hrms.holidaysModule.domain.mapper

import com.example.hrms.holidaysModule.data.model.PublicHolidaysResponse
import com.example.hrms.holidaysModule.data.model.HolidaysItem
import com.example.hrms.holidaysModule.domain.model.response.HolidaysDomainItem
import com.example.hrms.holidaysModule.domain.model.response.PublicHolidaysDomainResponse
import javax.inject.Inject

class HolidaysMapper @Inject constructor() {
    fun mapHolidaysResponse(response: PublicHolidaysResponse): PublicHolidaysDomainResponse {
        return PublicHolidaysDomainResponse(
            holidays = response.holidays?.mapNotNull { mapHolidayItem(it) },
            message = response.message,
            status = response.status
        )
    }

    fun mapHolidayItem(item: HolidaysItem?): HolidaysDomainItem? {
        return item?.let {
            HolidaysDomainItem(
                holidayDate = it.holiday_date,
                holidayName = it.holiday_name
            )
        }
    }
}
