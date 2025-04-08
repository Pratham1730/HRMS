package com.example.hrms.signUpModule.domain.model


data class PositionDomainResponse(
	val positions: List<PositionsDomainItem> = emptyList(),
	val message: String? = null,
	val status: Int? = null
)


data class PositionsDomainItem(
	val positionName: String? = null,
	val positionId: String? = null
)


