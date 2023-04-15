package com.fakhry.businessapp.core.network

import com.google.gson.annotations.SerializedName

data class ErrorResponse(

	@field:SerializedName("error")
	val error: Error
)

data class Error(

	@field:SerializedName("code")
	val code: String,

	@field:SerializedName("instance")
	val instance: String,

	@field:SerializedName("field")
	val field: String,

	@field:SerializedName("description")
	val description: String
)
