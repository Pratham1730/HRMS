package com.example.hrms.leaveModule.applyLeaveModule.domain.mapper

import com.example.hrms.leaveModule.applyLeaveModule.data.model.LeaveRequestResponse
import com.example.hrms.leaveModule.applyLeaveModule.data.model.LeaveTypeResponse
import com.example.hrms.leaveModule.applyLeaveModule.data.model.LeaveTypesItem
import com.example.hrms.leaveModule.applyLeaveModule.domain.model.response.LeaveRequestDomainResponse
import com.example.hrms.leaveModule.applyLeaveModule.domain.model.response.LeaveTypeDomainResponse
import com.example.hrms.leaveModule.applyLeaveModule.domain.model.response.LeaveTypesDomainItem
import javax.inject.Inject

class LeaveTypeMapper @Inject constructor() {
    fun mapLeaveTypeData(response: LeaveTypeResponse): LeaveTypeDomainResponse {
        return LeaveTypeDomainResponse(
            leaveType = response.leave_types?.map { item ->
                mapLeaveTypeItem(item!!)
            },
            message = response.message,
            status = response.status
        )
    }

    fun mapLeaveTypeItem(data: LeaveTypesItem): LeaveTypesDomainItem {
        return LeaveTypesDomainItem(
            id = data.id,
            typeName = data.type_name
        )
    }

    fun mapLeaveRequestData(response: LeaveRequestResponse): LeaveRequestDomainResponse {
        return LeaveRequestDomainResponse(
            message = response.message,
            status = response.status
        )
    }

}