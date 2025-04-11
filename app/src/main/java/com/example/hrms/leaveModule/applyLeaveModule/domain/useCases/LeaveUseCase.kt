package com.example.hrms.leaveModule.applyLeaveModule.domain.useCases

import com.example.hrms.leaveModule.applyLeaveModule.data.repository.LeaveRepositoryImpl
import com.example.hrms.leaveModule.applyLeaveModule.domain.model.response.LeaveTypeDomainResponse
import javax.inject.Inject

class LeaveUseCase @Inject constructor(private val leaveRepositoryImpl: LeaveRepositoryImpl){
    suspend fun invokeLeaveType(method : String) : LeaveTypeDomainResponse{
        return leaveRepositoryImpl.getLeaveType(method)
    }
}

