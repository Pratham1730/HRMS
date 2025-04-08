package com.example.hrms.signUpModule.domain.useCase

import com.example.hrms.signUpModule.data.repository.SignUpRepositoryImpl
import com.example.hrms.signUpModule.domain.model.DepartmentDomainModel
import com.example.hrms.signUpModule.domain.model.PositionDomainResponse
import javax.inject.Inject

class GetPositionUseCase @Inject constructor(private val signUpRepositoryImpl: SignUpRepositoryImpl) {
    suspend operator fun invoke(method : String , deptId : Int) : PositionDomainResponse {
        return signUpRepositoryImpl.getPosition(method , deptId)
    }
}