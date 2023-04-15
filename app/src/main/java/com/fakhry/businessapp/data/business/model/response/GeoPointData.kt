package com.fakhry.businessapp.data.business.model.response

import com.fakhry.businessapp.domain.business.model.GeoPoint
import com.google.gson.annotations.SerializedName

data class GeoPointData(

	@field:SerializedName("latitude")
	val latitude: Double,

	@field:SerializedName("longitude")
	val longitude: Double
)

fun GeoPointData.toDomain() = GeoPoint(latitude = latitude, longitude = longitude)