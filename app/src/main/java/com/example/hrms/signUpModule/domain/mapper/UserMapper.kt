package com.example.hrms.signUpModule.domain.mapper

import com.example.hrms.signUpModule.data.model.DepartmentModel
import com.example.hrms.signUpModule.data.model.DepartmentsItem
import com.example.hrms.signUpModule.data.model.PositionResponse
import com.example.hrms.signUpModule.data.model.PositionsItem
import com.example.hrms.signUpModule.domain.model.DepartmentDomainModel
import com.example.hrms.signUpModule.domain.model.DepartmentsDomainItem
import com.example.hrms.signUpModule.domain.model.PositionDomainResponse
import com.example.hrms.signUpModule.domain.model.PositionsDomainItem

class UserMapper {

    fun mapDepartmentData(response : DepartmentModel) : DepartmentDomainModel{
        return DepartmentDomainModel(
            departments = response.departments?.map {
                departmentsItem -> mapDepartmentItem(departmentsItem!!)
            },
            message = response.message,
            status = response.status
        )
    }

    fun mapDepartmentItem(data : DepartmentsItem) : DepartmentsDomainItem {
        return DepartmentsDomainItem(
            deptId = data.deptId,
            deptName = data.deptName
        )
    }

    fun mapPositionData(response: PositionResponse) : PositionDomainResponse{
        return PositionDomainResponse(
            positions = response.positions.map {
                positionsItem ->  mapPositionItem(positionsItem)
            },
            message = response.message,
            status = response.status
        )
    }

    fun mapPositionItem(data : PositionsItem) : PositionsDomainItem{
        return PositionsDomainItem(
            positionId = data.positionId,
            positionName = data.positionName
        )
    }

}