package com.example.hrms.leaveModule.displayLeaveModule.domain.useCases

import com.example.hrms.leaveModule.displayLeaveModule.data.repository.LeaveListRepositoryImpl
import com.example.hrms.leaveModule.displayLeaveModule.domain.model.response.LeaveDeleteDomainResponse
import com.example.hrms.leaveModule.displayLeaveModule.domain.model.response.LeaveListDomainResponse
import javax.inject.Inject

class GetLeaveListUseCase @Inject constructor(
    private val leaveListRepositoryImpl: LeaveListRepositoryImpl
) {
    suspend fun invokeLeaveList(method: String, userId: Int): LeaveListDomainResponse {
        return leaveListRepositoryImpl.getLeaveList(method, userId)
    }

    suspend fun invokeDeleteLeave(
        delete: String,
        leaveId: Int,
        userId: Int,
        companyId: Int
    ): LeaveDeleteDomainResponse {
        return leaveListRepositoryImpl.deleteLeaveRequest(delete, leaveId, userId, companyId)
    }
}
