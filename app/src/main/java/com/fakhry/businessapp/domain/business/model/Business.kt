package com.fakhry.businessapp.domain.business.model

import com.fakhry.businessapp.data.business.model.response.CategoriesData

data class Business(
    val id: String,
    val name: String,
    val imageUrl: String,
    val rating: Double,
    val reviewCount: Int,
    val coordinates: GeoPoint,
    val url: String,
    val displayPhone: String,
    val phone: String,
    val price: String,
    val alias: String,
    val fullAddress: String,
    val categories: List<CategoriesData>,
    val isClosed: Boolean
)

fun Business.ratingWithReviewCount() = "${this.rating} (${this.reviewCount})"

