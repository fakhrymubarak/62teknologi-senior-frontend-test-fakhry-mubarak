package com.fakhry.businessapp.data.business.model.response

import com.google.gson.annotations.SerializedName

data class Region(

	@field:SerializedName("center")
	val geopoint: GeoPointData
)