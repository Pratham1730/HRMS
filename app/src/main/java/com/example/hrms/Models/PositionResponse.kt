package com.example.hrms.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PositionResponse(
	@SerializedName("positions") val positions: List<PositionsItem> = emptyList(),
	@SerializedName("message") val message: String? = null,
	@SerializedName("status") val status: Int? = null
)


data class PositionsItem(
	@SerializedName("position_name") val positionName: String? = null,
	@SerializedName("position_id") val positionId: String? = null
)


