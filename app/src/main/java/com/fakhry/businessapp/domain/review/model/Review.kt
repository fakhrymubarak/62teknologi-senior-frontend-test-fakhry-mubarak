package com.fakhry.businessapp.domain.review.model

data class Review(
    val id: String,
    val userAvatarUrl: String,
    val userName: String,
    val rating: Int,
    val timeCreatedAgo: String,
    val text: String,
    val reviewUrl: String,
)