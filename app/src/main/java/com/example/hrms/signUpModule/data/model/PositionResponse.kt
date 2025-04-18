package com.example.hrms.signUpModule.data.model

import com.google.gson.annotations.SerializedName

data class PositionResponse(
	@SerializedName("positions") val positions: List<PositionsItem> = emptyList(),
	@SerializedName("message") val message: String? = null,
	@SerializedName("status") val status: Int? = null
)


data class PositionsItem(
	@SerializedName("position_name") val positionName: String? = null,
	@SerializedName("position_id") val positionId: String? = null
)


