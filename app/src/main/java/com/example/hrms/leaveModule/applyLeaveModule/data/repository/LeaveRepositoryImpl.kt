package com.example.hrms.leaveModule.applyLeaveModule.data.repository

import com.example.hrms.common.ApiService1
import com.example.hrms.leaveModule.applyLeaveModule.domain.mapper.LeaveTypeMapper
import com.example.hrms.leaveModule.applyLeaveModule.domain.model.response.LeaveRequestDomainResponse
import com.example.hrms.leaveModule.applyLeaveModule.domain.model.response.LeaveTypeDomainResponse
import javax.inject.Inject
import kotlin.math.exp

class LeaveRepositoryImpl @Inject constructor(private val apiService1: ApiService1 , private val mapper : LeaveTypeMapper) {

    suspend fun getLeaveType(method : String) : LeaveTypeDomainResponse{
        val response = apiService1.leaveType(method)

        if (response.isSuccessful){
            val responseBody = response.body()
            return mapper.mapLeaveTypeData(responseBody!!)
        }
        else{
            throw Exception("Error")
        }
    }


    suspend fun applyLeaveRequest(
        insert: String,
        companyId: Int,
        uId: Int,
        leaveTypeId: Int,
        lReason: String,
        lStartDate: String
    ): LeaveRequestDomainResponse {
        val response = apiService1.applyLeave(
            insert,
            companyId,
            uId,
            leaveTypeId,
            lReason,
            lStartDate
        )

        if (response.isSuccessful) {
            val responseBody = response.body()
            return responseBody?.let { mapper.mapLeaveRequestData(it) }
                ?: throw Exception("Empty response body")
        } else {
            throw Exception("Failed to apply leave: ${response.errorBody()?.string()}")
        }
    }
}