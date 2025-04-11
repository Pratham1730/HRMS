package com.example.hrms.signUpModule.domain.mapper

import com.example.hrms.signUpModule.data.model.ApiResponse
import com.example.hrms.signUpModule.data.model.CompanyItem
import com.example.hrms.signUpModule.data.model.CompanyResponse
import com.example.hrms.signUpModule.data.model.DepartmentModel
import com.example.hrms.signUpModule.data.model.DepartmentsItem
import com.example.hrms.signUpModule.data.model.PositionResponse
import com.example.hrms.signUpModule.data.model.PositionsItem
import com.example.hrms.signUpModule.domain.model.ApiDomainResponse
import com.example.hrms.signUpModule.domain.model.CompanyDomainItem
import com.example.hrms.signUpModule.domain.model.CompanyDomainResponse
import com.example.hrms.signUpModule.domain.model.DepartmentDomainModel
import com.example.hrms.signUpModule.domain.model.DepartmentsDomainItem
import com.example.hrms.signUpModule.domain.model.PositionDomainResponse
import com.example.hrms.signUpModule.domain.model.PositionsDomainItem
import javax.inject.Inject

class UserMapper @Inject constructor(){

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

    fun mapSignUpResponse(data : ApiResponse) : ApiDomainResponse{
        return ApiDomainResponse(
            status = data.status,
            message = data.message
        )
    }

    fun mapCompanyData(data : CompanyResponse) : CompanyDomainResponse{
        return CompanyDomainResponse(
            company = data.company?.map {
                companyItem -> mapCompanyList(companyItem!!)
            },
            message = data.message,
            status = data.status
        )
    }

    fun mapCompanyList(data : CompanyItem) : CompanyDomainItem{
        return CompanyDomainItem(
            companyId = data.company_id,
            companyName = data.company_name
        )
    }

}