package com.example.hrms.signInModule.data.repository

import com.example.hrms.common.ApiService1
import com.example.hrms.signInModule.domain.mapper.LoginMapper
import com.example.hrms.signInModule.domain.model.LoginDomainResponse
import javax.inject.Inject

class LoginUserImplementation  @Inject constructor(private val apiService1: ApiService1, private val mapper: LoginMapper){
    suspend fun signInUser(method : String , userEmail : String , userPassword : String) : LoginDomainResponse{
        val response = apiService1.setLogin(method  , userEmail , userPassword)

        if (response.isSuccessful){
            val responseBody = response.body()
            return mapper.mapLoginData(responseBody!!)
        }
        else{
            throw Exception("Failed")
        }
    }
}