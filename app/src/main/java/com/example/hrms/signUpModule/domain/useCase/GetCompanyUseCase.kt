package com.example.hrms.signUpModule.domain.useCase

import com.example.hrms.signUpModule.data.repository.SignUpRepositoryImpl
import com.example.hrms.signUpModule.domain.model.CompanyDomainResponse
import com.example.hrms.signUpModule.domain.model.DepartmentDomainModel
import javax.inject.Inject

class GetCompanyUseCase @Inject constructor(private val signUpRepositoryImpl: SignUpRepositoryImpl) {
    suspend operator fun invoke(method : String) : CompanyDomainResponse {
        return signUpRepositoryImpl.getCompany(method)
    }
}