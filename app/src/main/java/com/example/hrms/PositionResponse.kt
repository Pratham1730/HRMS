package com.example.hrms

import java.io.Serializable

data class PositionResponse(
	val positions: List<String?>? = null,
	val message: String? = null,
	val status: Int? = null
): Serializable

