package com.example.hrms.signInModule.domain.useCase

import com.example.hrms.signInModule.data.repository.LoginUserImplementation
import com.example.hrms.signInModule.domain.model.LoginDomainResponse
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(private val loginUserImplementation: LoginUserImplementation) {

    suspend operator fun invoke(method : String , userEmail : String , userPassword : String) : LoginDomainResponse{
        return loginUserImplementation.signInUser(method, userEmail , userPassword)
    }

}