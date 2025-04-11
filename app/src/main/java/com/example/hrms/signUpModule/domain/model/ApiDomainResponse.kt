package com.example.hrms.signUpModule.domain.model

import java.io.Serializable

data class ApiDomainResponse(
    val status: Int,
    val message: String
) : Serializable

