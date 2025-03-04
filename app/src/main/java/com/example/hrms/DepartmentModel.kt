package com.example.hrms

data class DepartmentModel(
	val departments: List<DepartmentsItem?>? = null,
	val message: String? = null,
	val status: Int? = null
)

data class DepartmentsItem(
	val deptName: String? = null,
	val deptId: String? = null
)

