package com.example.hrms.leaveModule.displayLeaveModule.data.repository

import com.example.hrms.common.ApiService1
import com.example.hrms.leaveModule.displayLeaveModule.domain.mapper.LeaveListMapper
import com.example.hrms.leaveModule.displayLeaveModule.domain.model.response.LeaveDeleteDomainResponse
import com.example.hrms.leaveModule.displayLeaveModule.domain.model.response.LeaveListDomainResponse
import javax.inject.Inject

class LeaveListRepositoryImpl @Inject constructor(
    private val apiService1: ApiService1,
    private val mapper: LeaveListMapper
) {
    suspend fun getLeaveList(method: String, userId: Int): LeaveListDomainResponse {
        val response = apiService1.selectLeave(method, userId)

        if (response.isSuccessful) {
            val responseBody = response.body()
            return responseBody?.let { mapper.mapLeaveListData(it) }
                ?: throw Exception("Empty response body")
        } else {
            throw Exception("Failed to fetch leave list: ${response.errorBody()?.string()}")
        }
    }

    suspend fun deleteLeaveRequest(
        delete: String,
        leaveId: Int,
        userId: Int,
        companyId: Int
    ): LeaveDeleteDomainResponse {
        val response = apiService1.deleteLeave(delete, leaveId, userId, companyId)

        if (response.isSuccessful) {
            val responseBody = response.body()
            return responseBody?.let { mapper.mapLeaveDeleteResponse(it) }
                ?: throw Exception("Empty response body")
        } else {
            throw Exception("Failed to delete leave: ${response.errorBody()?.string()}")
        }
    }
}
