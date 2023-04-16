package com.fakhry.businessapp.data.business.model.response

import com.google.gson.annotations.SerializedName

data class OpenItem(

	@field:SerializedName("is_overnight")
	val isOvernight: Boolean,

	@field:SerializedName("start")
	val start: String,

	@field:SerializedName("end")
	val end: String,

	@field:SerializedName("day")
	val day: Int
)