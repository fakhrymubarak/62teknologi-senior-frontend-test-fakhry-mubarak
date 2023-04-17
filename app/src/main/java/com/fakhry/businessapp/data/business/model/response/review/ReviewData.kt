package com.fakhry.businessapp.data.business.model.response.review

import com.fakhry.businessapp.core.utils.toDateAgo
import com.fakhry.businessapp.domain.review.model.Review
import com.google.gson.annotations.SerializedName

data class ReviewData(

    @field:SerializedName("rating")
    val rating: Int,

    @field:SerializedName("time_created")
    val timeCreated: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("text")
    val text: String,

    @field:SerializedName("user")
    val userData: UserData,

    @field:SerializedName("url")
    val url: String
)

private fun ReviewData.toDomain() = Review(
    rating = this.rating,
    id = this.id,
    text = this.text,
    timeCreatedAgo =  timeCreated.toDateAgo(),
    userName = this.userData.name,
    userAvatarUrl = this.userData.imageUrl ?: "https://icon-library.com/images/default-profile-icon/default-profile-icon-24.jpg",
)

fun List<ReviewData>.toDomain() = this.map { it.toDomain() }