package com.example.hrms.responses

import java.io.Serializable

data class UserDataResponse(
	val message: String? = null,
	val user: UserData? = null,
	val status: Int? = null
) : Serializable

data class UserData(
	val u_name: String? = null,
	val u_modified_by: String? = null,
	val u_is_delete: String? = null,
	val company_id: String? = null,
	val u_img: String? = null,
	val u_email: String? = null,
	val position_name: String? = null,
	val dept_name: String? = null,
	val u_pass: String? = null,
	val u_dob: String? = null,
	val u_id: String? = null,
	val u_phone: String? = null,
	val u_gender: String? = null,
	val u_salary: String? = null,
	val u_created_date: String? = null,
	val dept_id: String? = null,
	val u_joining_Date: String? = null,
	val position_id: String? = null
) : Serializable

