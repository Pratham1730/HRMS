package com.example.hrms.responses

import java.io.Serializable

data class DepartmentEmployeeResponse(
	val employees: List<EmployeesItem?>? = null,
	val message: String? = null,
	val status: Int? = null
): Serializable

data class EmployeesItem(
	val user_id: String? = null,
	val user_name: String? = null,
	val user_position: String? = null
): Serializable

