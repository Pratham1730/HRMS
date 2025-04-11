package com.example.hrms.signInModule.domain.mapper

import com.example.hrms.signInModule.data.model.LoginResponse
import com.example.hrms.signInModule.data.model.User
import com.example.hrms.signInModule.domain.model.LoginDomainResponse
import com.example.hrms.signInModule.domain.model.UserDomain
import javax.inject.Inject

class LoginMapper @Inject constructor(){

    fun mapLoginData(data : LoginResponse) : LoginDomainResponse{
        return LoginDomainResponse(
            message = data.message,
            user = data.user?.let { mapUser(it) },
            status = data.status
        )
    }

    fun mapUser(user : User) : UserDomain {
        return UserDomain(
            uName = user.u_name,
            uEmail = user.u_email,
            uId = user.u_id,
            companyId = user.company_id
        )
    }

    /*
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
    */

}