package com.fakhry.businessapp.data.business.model.response

import com.google.gson.annotations.SerializedName

data class HoursItem(

	@field:SerializedName("is_open_now")
	val isOpenNow: Boolean,

	@field:SerializedName("hours_type")
	val hoursType: String,

	@field:SerializedName("open")
	val open: List<OpenItem>
)