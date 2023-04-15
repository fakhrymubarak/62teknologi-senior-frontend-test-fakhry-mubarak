package com.fakhry.businessapp.data.business.model.response

import com.google.gson.annotations.SerializedName

data class CategoriesData(

	@field:SerializedName("alias")
	val alias: String,

	@field:SerializedName("title")
	val title: String
)