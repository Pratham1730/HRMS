package com.example.hrms.signUpModule.data.repository

import com.example.hrms.common.ApiService1
import com.example.hrms.signUpModule.domain.mapper.UserMapper
import com.example.hrms.signUpModule.domain.model.DepartmentDomainModel
import com.example.hrms.signUpModule.domain.model.PositionDomainResponse
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val apiService1: ApiService1,
    private val mapper : UserMapper
){
    suspend fun getDept(
        method : String,
        companyId : Int
    ) : DepartmentDomainModel {
        val response = apiService1.setDept(method , companyId)

        if (response.isSuccessful){
            val responseBody = response.body()
            return mapper.mapDepartmentData(responseBody!!)
        }
        else{
            throw Exception("Failed to add user")
        }
    }

    suspend fun getPosition(
        method: String,
        deptId : Int
    ) : PositionDomainResponse {
        val response = apiService1.getPosition(method , deptId)
        if (response.isSuccessful){
            val responseBody = response.body()
            return mapper.mapPositionData(responseBody!!)
        }
        else{
            throw Exception("Failed to add user")
        }
    }
}