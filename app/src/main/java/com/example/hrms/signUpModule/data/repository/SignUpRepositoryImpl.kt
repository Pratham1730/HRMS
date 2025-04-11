package com.example.hrms.signUpModule.data.repository

import com.example.hrms.common.ApiService1
import com.example.hrms.signUpModule.domain.mapper.UserMapper
import com.example.hrms.signUpModule.domain.model.ApiDomainResponse
import com.example.hrms.signUpModule.domain.model.CompanyDomainResponse
import com.example.hrms.signUpModule.domain.model.DepartmentDomainModel
import com.example.hrms.signUpModule.domain.model.PositionDomainResponse
import java.math.BigInteger
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val apiService1: ApiService1,
    private val mapper : UserMapper
){

    suspend fun getCompany(
        method: String
    ) : CompanyDomainResponse{
        val response = apiService1.selectCompany(method)

        if (response.isSuccessful){
            val responseBody = response.body()
            return mapper.mapCompanyData(responseBody!!)
        }
        else{
            throw Exception("Failed")
        }
    }

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
            throw Exception("Failed")
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
            throw Exception("Failed")
        }
    }

    suspend fun signUpUser(
        method: String,
        name : String,
        mobile : BigInteger,
        email : String,
        department : Int,
        gender : Int,
        position : Int,
        salary : Int,
        dob : String,
        joining : String,
        password : String,
        company : Int
    ) : ApiDomainResponse {
        val response = apiService1.signUpUser(method,name , email, password , mobile , gender , department , position , salary , joining , dob , company)

        if (response.isSuccessful){
            val responseBody = response.body()
            return mapper.mapSignUpResponse(responseBody!!)
        }
        else{
            throw Exception("Failed")
        }
    }
}