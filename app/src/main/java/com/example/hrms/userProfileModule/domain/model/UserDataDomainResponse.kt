package com.example.hrms.userProfileModule.domain.model

import java.io.Serializable

data class UserDataDomainResponse(
	val message: String? = null,
	val user: UserDomainData? = null,
	val status: Int? = null
) : Serializable

data class UserDomainData(
	val userName: String? = null,
	val modifiedBy: String? = null,
	val isDeleted: String? = null,
	val companyId: String? = null,
	val image: String? = null,
	val email: String? = null,
	val positionName: String? = null,
	val departmentName: String? = null,
	val password: String? = null,
	val dateOfBirth: String? = null,
	val userId: String? = null,
	val phone: String? = null,
	val gender: String? = null,
	val salary: String? = null,
	val createdDate: String? = null,
	val departmentId: String? = null,
	val joiningDate: String? = null,
	val positionId: String? = null
) : Serializable

