package com.example.hrms.responses

import com.google.gson.annotations.SerializedName

data class DepartmentModel(
	@SerializedName("departments") val departments: List<DepartmentsItem?>? = null,
	@SerializedName("message") val message: String? = null,
	@SerializedName("status") val status: Int? = null
)

data class DepartmentsItem(
	@SerializedName("dept_name") val deptName: String? = null,
	@SerializedName("dept_id") val deptId: String? = null
)
