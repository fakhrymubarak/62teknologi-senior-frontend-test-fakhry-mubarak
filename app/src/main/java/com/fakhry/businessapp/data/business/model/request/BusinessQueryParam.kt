package com.fakhry.businessapp.data.business.model.request

import com.fakhry.businessapp.core.enums.API_DEFAULT_LOCATION
import com.fakhry.businessapp.data.business.remote.SortBy
import com.google.gson.annotations.SerializedName

data class BusinessQueryParam(
    @SerializedName("term")
    var terms: String = "",

    @SerializedName("location")
    val location: String = API_DEFAULT_LOCATION,

    @SerializedName("sort_by")
    var sorter: String = SortBy.BEST_MATCH.value,
)

fun BusinessQueryParam.asMap() = mapOf<String, String>(
    "term" to this.terms,
    "location" to this.location,
    "sort_by" to this.sorter,
)