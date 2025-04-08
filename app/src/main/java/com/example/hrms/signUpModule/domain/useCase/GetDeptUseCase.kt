package com.example.hrms.signUpModule.domain.useCase

import com.example.hrms.signUpModule.data.repository.SignUpRepositoryImpl
import com.example.hrms.signUpModule.domain.model.DepartmentDomainModel
import javax.inject.Inject

class GetDeptUseCase @Inject constructor(private val signUpRepositoryImpl: SignUpRepositoryImpl) {
    suspend operator fun invoke(method : String , companyId : Int) : DepartmentDomainModel{
        return signUpRepositoryImpl.getDept(method , companyId)
    }
}