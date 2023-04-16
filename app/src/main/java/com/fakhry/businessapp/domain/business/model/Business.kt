package com.fakhry.businessapp.domain.business.model

data class Business(
    val id: String,
    val name: String,
    val imageUrl: String,
    val rating: Double,
    val reviewCount: Int,
    val displayPhone: String,
    val fullAddress: String,
)

fun Business.ratingWithReviewCount() = "${this.rating} (${this.reviewCount})"

