package com.fakhry.businessapp.data.business.model.response

import com.fakhry.businessapp.domain.business.model.Business
import com.google.gson.annotations.SerializedName

data class BusinessesData(

    @field:SerializedName("distance")
    val distance: Double,

    @field:SerializedName("image_url")
    val imageUrl: String,

    @field:SerializedName("rating")
    val rating: Double,

    @field:SerializedName("coordinates")
    val coordinates: GeoPointData,

    @field:SerializedName("review_count")
    val reviewCount: Int,

    @field:SerializedName("transactions")
    val transactions: List<String>,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("display_phone")
    val displayPhone: String,

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("price")
    val price: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("alias")
    val alias: String,

    @field:SerializedName("location")
    val location: LocationData,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("categories")
    val categories: List<CategoriesData>,

    @field:SerializedName("is_closed")
    val isClosed: Boolean
)

fun BusinessesData.toDomain() = Business(
    id = id,
    name = name,
    imageUrl = imageUrl,
    rating = rating,
    reviewCount = reviewCount,
    coordinates = coordinates.toDomain(),
    url = url,
    displayPhone = displayPhone,
    phone = phone,
    price = price,
    alias = alias,
    fullAddress = location.toFullAddress(),
    categories = categories,
    isClosed = isClosed,
)