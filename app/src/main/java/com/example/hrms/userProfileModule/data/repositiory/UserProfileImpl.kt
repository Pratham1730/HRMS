package com.example.hrms.userProfileModule.data.repositiory

import com.example.hrms.common.ApiService1
import com.example.hrms.userProfileModule.domain.mapper.UserDataMapper
import com.example.hrms.userProfileModule.domain.model.UserDataDomainResponse
import javax.inject.Inject

class UserProfileImpl @Inject constructor(private val apiService1: ApiService1 , private val mapper : UserDataMapper){

    suspend fun getUserProfile(method : String , userEmail : String) : UserDataDomainResponse{
        val response = apiService1.getUserData(method , userEmail)
        if (response.isSuccessful){
            val responseBody = response.body()
            return mapper.mapUserDataResponse(responseBody!!)
        }
        else{
            throw Exception("Failed")
        }
    }

}