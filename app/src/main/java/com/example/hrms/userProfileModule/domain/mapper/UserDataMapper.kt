package com.example.hrms.userProfileModule.domain.mapper

import com.example.hrms.userProfileModule.data.model.UserData
import com.example.hrms.userProfileModule.data.model.UserDataResponse
import com.example.hrms.userProfileModule.domain.model.UserDataDomainResponse
import com.example.hrms.userProfileModule.domain.model.UserDomainData
import javax.inject.Inject

class UserDataMapper @Inject constructor() {

    fun mapUserDataResponse(response: UserDataResponse):  UserDataDomainResponse{
        return UserDataDomainResponse(
            message = response.message,
            user = response.user?.let { mapUserData(it) },
            status = response.status
        )
    }

    fun mapUserData(data: UserData): UserDomainData {
        return UserDomainData(
            userName = data.u_name,
            modifiedBy = data.u_modified_by,
            isDeleted = data.u_is_delete,
            companyId = data.company_id,
            image = data.u_img,
            email = data.u_email,
            positionName = data.position_name,
            departmentName = data.dept_name,
            password = data.u_pass,
            dateOfBirth = data.u_dob,
            userId = data.u_id,
            phone = data.u_phone,
            gender = data.u_gender,
            salary = data.u_salary,
            createdDate = data.u_created_date,
            departmentId = data.dept_id,
            joiningDate = data.u_joining_Date,
            positionId = data.position_id
        )
    }


}