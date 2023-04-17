package com.fakhry.businessapp.data.business.model.response.review

import com.google.gson.annotations.SerializedName

data class ReviewResponse(

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("possible_languages")
	val possibleLanguages: List<String>,

	@field:SerializedName("reviews")
	val reviews: List<ReviewData>
)