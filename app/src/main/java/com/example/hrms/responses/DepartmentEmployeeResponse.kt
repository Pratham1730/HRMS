package com.example.hrms.responses

data class DepartmentEmployeeResponse(
	val employees: List<EmployeesItem?>? = null,
	val message: String? = null,
	val status: Int? = null
)

data class EmployeesItem(
	val userId: String? = null,
	val userName: String? = null,
	val userPosition: String? = null
)

