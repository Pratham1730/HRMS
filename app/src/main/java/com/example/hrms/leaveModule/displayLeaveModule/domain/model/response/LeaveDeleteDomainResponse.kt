package com.example.hrms.leaveModule.displayLeaveModule.domain.model.response

import java.io.Serializable

data class LeaveDeleteDomainResponse(
    val message: String? = null,
    val status: Int? = null
) : Serializable
