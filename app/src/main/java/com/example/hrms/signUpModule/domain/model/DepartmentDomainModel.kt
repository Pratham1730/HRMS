package com.example.hrms.signUpModule.domain.model

import com.google.gson.annotations.SerializedName

data class DepartmentDomainModel(
	val departments: List<DepartmentsDomainItem?>? = null,
	val message: String? = null,
	val status: Int? = null
)

data class DepartmentsDomainItem(
	val deptName: String? = null,
	val deptId: String? = null
)
