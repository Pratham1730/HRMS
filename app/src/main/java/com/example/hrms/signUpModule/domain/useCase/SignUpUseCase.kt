package com.example.hrms.signUpModule.domain.useCase

import com.example.hrms.signUpModule.data.repository.SignUpRepositoryImpl
import com.example.hrms.signUpModule.domain.model.ApiDomainResponse
import java.math.BigInteger
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val signUpRepositoryImpl: SignUpRepositoryImpl){
    suspend operator fun invoke(
        method : String,
        name: String,
        mobile: BigInteger,
        email: String,
        department: Int,
        gender: Int,
        position: Int,
        salary: Int,
        dob: String,
        joining: String,
        password: String,
        company: Int
    ): ApiDomainResponse {
        return signUpRepositoryImpl.signUpUser(method , name , mobile , email , department , gender , position , salary , dob , joining , password , company)
    }
}