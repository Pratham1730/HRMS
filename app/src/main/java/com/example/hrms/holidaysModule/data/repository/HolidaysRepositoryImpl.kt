package com.example.hrms.holidaysModule.data.repository

import com.example.hrms.common.ApiService1
import com.example.hrms.holidaysModule.domain.mapper.HolidaysMapper
import com.example.hrms.holidaysModule.domain.model.response.PublicHolidaysDomainResponse
import javax.inject.Inject

class HolidaysRepositoryImpl @Inject constructor(
    private val apiService1: ApiService1,
    private val mapper: HolidaysMapper
) {
    suspend fun getPublicHolidays(method: String, companyId: Int): PublicHolidaysDomainResponse {
        val response = apiService1.getHolidays(method, companyId)

        if (response.isSuccessful) {
            val responseBody = response.body()
            return responseBody?.let { mapper.mapHolidaysResponse(it) }
                ?: throw Exception("Empty response body")
        } else {
            throw Exception("Failed to fetch holidays: ${response.errorBody()?.string()}")
        }
    }
}
