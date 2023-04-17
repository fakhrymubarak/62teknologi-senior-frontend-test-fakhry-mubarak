package com.fakhry.businessapp.data.business.model.response.review

import com.google.gson.annotations.SerializedName

data class UserData(

    @field:SerializedName("profile_url")
    val profileUrl: String,

    @field:SerializedName("image_url")
    val imageUrl: String?,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String
)