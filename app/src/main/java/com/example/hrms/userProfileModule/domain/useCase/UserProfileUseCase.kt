package com.example.hrms.userProfileModule.domain.useCase

import com.example.hrms.userProfileModule.data.repositiory.UserProfileImpl
import com.example.hrms.userProfileModule.domain.model.UserDataDomainResponse
import javax.inject.Inject

class UserProfileUseCase @Inject constructor(private val userProfileImpl: UserProfileImpl){

    suspend operator fun invoke(method : String , userEmail : String) : UserDataDomainResponse{
        return userProfileImpl.getUserProfile(method , userEmail)
    }

}