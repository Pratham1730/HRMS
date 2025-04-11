package com.example.hrms.updateUserModule.domain.mapper

import com.example.hrms.updateUserModule.data.model.UpdateDataResponse
import com.example.hrms.updateUserModule.domain.model.UpdateDataDomainResponse
import javax.inject.Inject

class UpdateUserMapper @Inject constructor() {
    fun mapDepartmentItem(data : UpdateDataResponse) : UpdateDataDomainResponse{
        return UpdateDataDomainResponse(
            status = data.status,
            message = data.message
        )
    }
}