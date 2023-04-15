package com.fakhry.businessapp.data.business.model.response

import com.google.gson.annotations.SerializedName

data class LocationData(

    @field:SerializedName("country")
    val country: String,

    @field:SerializedName("address3")
    val address3: String?,

    @field:SerializedName("address2")
    val address2: String?,

    @field:SerializedName("city")
    val city: String,

    @field:SerializedName("address1")
    val address1: String,

    @field:SerializedName("display_address")
    val displayAddress: List<String>,

    @field:SerializedName("state")
    val state: String,

    @field:SerializedName("zip_code")
    val zipCode: String
)

fun LocationData.toFullAddress(): String {
	return if (address3.isNullOrEmpty() && !address2.isNullOrEmpty()) {
		"$address1, $address2, $city, $state, $country, $zipCode"
	} else if (address2.isNullOrEmpty()) {
		"$address1, $city, $state, $country, $zipCode"
	} else if (address1.isEmpty()) {
		"$address1, $city, $state, $country, $zipCode"
	} else {
		"$city, $state, $country, $zipCode"
	}
}