package com.example.hrms.leaveModule.displayLeaveModule.domain.model.response

import java.io.Serializable

data class LeaveListDomainResponse(
    val message: String? = null,
    val leaveData: List<LeaveDataDomainItem?>? = null,
    val status: Int? = null
) : Serializable

data class LeaveDataDomainItem(
    val statusId: Int? = null,
    val userId: Int? = null,
    val reason: String? = null,
    val companyId: Int? = null,
    val leaveType: String? = null,
    val leaveStatus: String? = null,
    val startDate: String? = null,
    val leaveId: Int? = null,
    val approvedBy: Any? = null,
    val leaveTypeId: Int? = null
) : Serializable
