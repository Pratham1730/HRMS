package com.example.hrms.holidaysModule.domain.useCases

import com.example.hrms.holidaysModule.data.repository.HolidaysRepositoryImpl
import com.example.hrms.holidaysModule.domain.model.response.PublicHolidaysDomainResponse
import javax.inject.Inject

class GetHolidaysUseCase @Inject constructor(
    private val holidaysRepositoryImpl: HolidaysRepositoryImpl
) {
    suspend fun invokeGetHolidays(method: String, companyId: Int): PublicHolidaysDomainResponse {
        return holidaysRepositoryImpl.getPublicHolidays(method, companyId)
    }
}
