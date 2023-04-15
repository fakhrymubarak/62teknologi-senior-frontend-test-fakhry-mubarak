package com.fakhry.businessapp.data.business.model.response

import com.google.gson.annotations.SerializedName

data class BusinessesResponse(

    @field:SerializedName("total")
	val total: Int,

    @field:SerializedName("region")
	val region: Region,

    @field:SerializedName("businesses")
	val businesses: List<BusinessesData>
)