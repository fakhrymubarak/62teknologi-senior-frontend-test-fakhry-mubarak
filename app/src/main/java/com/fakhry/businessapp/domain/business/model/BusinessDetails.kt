package com.fakhry.businessapp.domain.business.model

data class BusinessDetails(
    val business: Business,
    val photoUrls: List<String>,
    val isOpenNow: Boolean,
    val categories: List<String>,
)
