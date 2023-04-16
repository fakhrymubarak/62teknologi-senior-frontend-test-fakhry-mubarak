package com.fakhry.businessapp.data.business.remote

import com.fakhry.businessapp.core.enums.API_SEARCH_LIMIT
import com.fakhry.businessapp.data.business.model.response.BusinessesData
import com.fakhry.businessapp.data.business.model.response.BusinessesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

enum class SortBy(val value: String) {
    BEST_MATCH("best_match"),
    RATING("rating"),
    REVIEW_COUNT("review_count"),
    DISTANCE("distance"),
}

interface BusinessApiService {

    @GET("v3/businesses/search")
    suspend fun getBusiness(
        @QueryMap queryParam: Map<String, String>,
        @Query("limit") limit: Int = API_SEARCH_LIMIT,
        @Query("offset") offset: Int = 0,
        @Query("attributes") filters: List<String> = listOf(""),
    ): BusinessesResponse

    @GET("v3/businesses/{id}")
    suspend fun getBusinessDetails(
        @Path("id") id: String,
    ): BusinessesData
}
