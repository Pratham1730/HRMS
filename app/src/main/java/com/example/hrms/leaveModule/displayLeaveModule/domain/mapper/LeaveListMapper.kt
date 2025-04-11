package com.example.hrms.leaveModule.displayLeaveModule.domain.mapper

import com.example.hrms.leaveModule.displayLeaveModule.data.model.LeaveDataItem
import com.example.hrms.leaveModule.displayLeaveModule.data.model.LeaveDeteleResponse
import com.example.hrms.leaveModule.displayLeaveModule.data.model.LeaveListResponse
import com.example.hrms.leaveModule.displayLeaveModule.domain.model.response.LeaveDataDomainItem
import com.example.hrms.leaveModule.displayLeaveModule.domain.model.response.LeaveDeleteDomainResponse
import com.example.hrms.leaveModule.displayLeaveModule.domain.model.response.LeaveListDomainResponse
import javax.inject.Inject

class LeaveListMapper @Inject constructor() {

    fun mapLeaveListData(response: LeaveListResponse): LeaveListDomainResponse {
        return LeaveListDomainResponse(
            message = response.message,
            status = response.status,
            leaveData = response.leave_data?.mapNotNull { it?.let { item -> mapLeaveDataItem(item) } }
        )
    }

    private fun mapLeaveDataItem(data: LeaveDataItem): LeaveDataDomainItem {
        return LeaveDataDomainItem(
            statusId = data.l_status_id,
            userId = data.u_id,
            reason = data.l_reason,
            companyId = data.company_id,
            leaveType = data.leave_type,
            leaveStatus = data.leave_status,
            startDate = data.l_start_date,
            leaveId = data.l_id,
            approvedBy = data.l_approved_by,
            leaveTypeId = data.leave_type_id
        )
    }

    fun mapLeaveDeleteResponse(response: LeaveDeteleResponse): LeaveDeleteDomainResponse {
        return LeaveDeleteDomainResponse(
            message = response.message,
            status = response.status
        )
    }
}
